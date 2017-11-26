package model;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import wave_file_reader_from_web.WavFile;

public class Model {

	private double lengthOfFile;
	private double lengthOfFileMiliseconds;
	private WavFile wavFile;
	private File fileToRead;
	private ArrayList<ResultDFT> results;
	private int timeSelectedMs, sampleRate, powerOfTwo;
	private boolean generatedSignal = false;
	private double[] firstGenerated, generatedSignalCurrent;

	public Model() {
		super();
		core();
	}

	public void core() {

	}

	public void readFile(String fileName) {
		if (fileName.equals("generated signal")) {
			generatedSignal = true;
			firstGenerated = generateSignal();
		} else {
			try {
				fileToRead = new File(fileName + ".wav");
				openFileAndResetIt();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void openFileAndResetIt() {
		if(generatedSignal){
			generatedSignalCurrent = firstGenerated;
			lengthOfFile = 1;
			sampleRate = 44100;
		}
		else{
			try {
				wavFile = WavFile.openWavFile(fileToRead);
				lengthOfFile = (double) wavFile.getNumFrames() / wavFile.getSampleRate();
				sampleRate = (int) wavFile.getSampleRate();
	
				wavFile.display();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public double getLengthOfFile() {
		return lengthOfFile;
	}

	public void setLengthOfFile(double lengthOfFile) {
		this.lengthOfFile = lengthOfFile;
	}

	public void readingHistogram(int from, int to, String funkcija) {
		try {
			openFileAndResetIt();
			int preskociFrejmove, doFrejmova, windowSampleNum;
			lengthOfFileMiliseconds = lengthOfFile * 1000;
			if (from == -1 || to == -1) {
				from = 0;
				to = (int) lengthOfFileMiliseconds;
				preskociFrejmove = 0;
				if(generatedSignal){
					doFrejmova = 44100;
					windowSampleNum = 44100;
				}else{
					doFrejmova = (int) wavFile.getNumFrames();
					windowSampleNum = (int) wavFile.getNumFrames();
				}
			} else {
				double proporcijaVremena = ((double) from * 100) / lengthOfFileMiliseconds;
				if(generatedSignal) preskociFrejmove = (int) (44100 * proporcijaVremena / 100);
				else preskociFrejmove = (int) (wavFile.getNumFrames() * proporcijaVremena / 100);
				double proporcijaVremenaKraj = ((double) to * 100) / lengthOfFileMiliseconds;
				if(generatedSignal)doFrejmova = (int) (44100 * proporcijaVremenaKraj / 100);
				else doFrejmova = (int) (wavFile.getNumFrames() * proporcijaVremenaKraj / 100);
				windowSampleNum = doFrejmova - preskociFrejmove;
			}
			timeSelectedMs = to - from;
			int numChannels = wavFile.getNumChannels();
			int framesRead;

			double[] noviRealniZaBrziDFT;
			double[] img;
			results = new ArrayList<>();
			if(!generatedSignal){
				// SKIPING//////////
				double[] buffer = new double[preskociFrejmove * numChannels];
				framesRead = wavFile.readFrames(buffer, preskociFrejmove);
				/////////////////
				buffer = new double[(doFrejmova - preskociFrejmove) * numChannels];
//				results = new ArrayList<>();
				double[] real;
	
				framesRead = wavFile.readFrames(buffer, (doFrejmova - preskociFrejmove));
				img = new double[framesRead * numChannels];
				real = new double[framesRead * numChannels];
	
				for (int s = 0; s < framesRead * numChannels; s++) {
					img[s] = 0;
					real[s] = buffer[s];
	
					////// FNKCIJA
					if (funkcija.equals("Hamming")) {
						real[s] = ((double) (real[s] * (0.54 - 0.46 * Math.cos(2.0 * Math.PI * s / windowSampleNum))));
					} else if (funkcija.equals("Hanning")) {
						real[s] = ((double) (real[s] * 0.5 * (1 - Math.cos(2.0 * Math.PI * s / windowSampleNum))));
					}
					////////////
				}
	
				noviRealniZaBrziDFT = real.clone();
			} else{
//				results = new ArrayList<>();
				double[] real = new double[doFrejmova-preskociFrejmove];
				img = new double[doFrejmova-preskociFrejmove];
				for(int k = preskociFrejmove;k<doFrejmova; k++){
					img[k-preskociFrejmove] = 0;
					real[k-preskociFrejmove] = generatedSignalCurrent[k];
					////// FNKCIJA
						if (funkcija.equals("Hamming")) {
							real[k-preskociFrejmove] = ((double) (real[k-preskociFrejmove] * (0.54 - 0.46 * Math.cos(2.0 * Math.PI * (k-preskociFrejmove) / windowSampleNum))));
						} else if (funkcija.equals("Hanning")) {
							real[k-preskociFrejmove] = ((double) (real[k-preskociFrejmove] * 0.5 * (1 - Math.cos(2.0 * Math.PI * (k-preskociFrejmove) / windowSampleNum))));
						}
					////////////
				}
			
				noviRealniZaBrziDFT = real.clone();
			}
			DFT(1, noviRealniZaBrziDFT.length, noviRealniZaBrziDFT, img);
			results.add(new ResultDFT(noviRealniZaBrziDFT, img));
			wavFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void readingSonogram(int from, int to, String funkcija) {
		if(!generatedSignal){
		try {
			openFileAndResetIt();
			int preskociFrejmove, doFrejmova, windowSampleNum;
			lengthOfFileMiliseconds = lengthOfFile * 1000;
			if (from == -1 || to == -1) {
				from = 0;
				to = (int) lengthOfFileMiliseconds;
				preskociFrejmove = 0;
				doFrejmova = (int) wavFile.getNumFrames();
				windowSampleNum = (int) wavFile.getNumFrames();
			} else {
				double proporcijaVremena = ((double) from * 100) / lengthOfFileMiliseconds;
				preskociFrejmove = (int) (wavFile.getNumFrames() * proporcijaVremena / 100);
				double proporcijaVremenaKraj = ((double) to * 100) / lengthOfFileMiliseconds;
				doFrejmova = (int) (wavFile.getNumFrames() * proporcijaVremenaKraj / 100);
				windowSampleNum = doFrejmova - preskociFrejmove;
			}
			int numChannels = wavFile.getNumChannels();
			int framesRead;

			// SKIPING//////////
			double[] buffer = new double[preskociFrejmove * numChannels];
			framesRead = wavFile.readFrames(buffer, preskociFrejmove);
			/////////////////
			
			int ostaliFrejmovi = doFrejmova - preskociFrejmove;
			results = new ArrayList<>();
			int currentNumberToRead;
			while(ostaliFrejmovi>0){
				if(ostaliFrejmovi>powerOfTwo)
					currentNumberToRead = powerOfTwo;
				else currentNumberToRead = powerOfTwo;
//					currentNumberToRead = ostaliFrejmovi;
				
				buffer = new double[currentNumberToRead * numChannels];
				
				double[] real, img;
	
				framesRead = wavFile.readFrames(buffer, currentNumberToRead);
				img = new double[framesRead * numChannels];
				real = new double[framesRead * numChannels];
	
				for (int s = 0; s < framesRead * numChannels; s++) {
					img[s] = 0;
					real[s] = buffer[s];
	
					////// FNKCIJA
					if (funkcija.equals("Hamming")) {
						real[s] = ((double) (real[s] * (0.54 - 0.46 * Math.cos(2.0 * Math.PI * s / windowSampleNum))));
					} else if (funkcija.equals("Hanning")) {
						real[s] = ((double) (real[s] * 0.5 * (1 - Math.cos(2.0 * Math.PI * s / windowSampleNum))));
					}
					////////////
				}
	
				double[] noviRealniZaBrziDFT = real.clone();
	
				DFT(1, noviRealniZaBrziDFT.length, noviRealniZaBrziDFT, img);
				results.add(new ResultDFT(noviRealniZaBrziDFT, img));
				ostaliFrejmovi-=powerOfTwo;
			}
			wavFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}}
		
	}

	public ArrayList<ResultDFT> getResults() {
		return results;
	}

	public int getTimeSelectedMs() {
		return timeSelectedMs;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setPowerOfTwo(int powerOfTwo) {
		this.powerOfTwo = powerOfTwo;
	}

	public int getPowerOfTwo() {
		return powerOfTwo;
	}

	public static void DFT(int dir, int m, double x1[], double y1[]) {
		DecimalFormat df = new DecimalFormat("#.000000");
		int i, k;
		double arg;
		double cosarg, sinarg;
		double x2[] = new double[m];
		double y2[] = new double[m];

		for (i = 0; i < m; i++) {
			x2[i] = 0;
			y2[i] = 0;
			arg = -dir * 2.0 * Math.PI * (double) i / (double) m;
			for (k = 0; k < m; k++) {
				cosarg = Math.cos(k * arg);
				sinarg = Math.sin(k * arg);
				x2[i] += (x1[k] * cosarg - y1[k] * sinarg);
				y2[i] += (x1[k] * sinarg + y1[k] * cosarg);
			}
		}
		if (dir == 1) {
			for (i = 0; i < m; i++) {
				x1[i] = Double.parseDouble(df.format(x2[i] / (double) m));
				y1[i] = Double.parseDouble(df.format(y2[i] / (double) m));
			}
		} else {
			for (i = 0; i < m; i++) {
				x1[i] = Double.parseDouble(df.format(x2[i]));
				y1[i] = Double.parseDouble(df.format(y2[i]));
			}
		}

	}
	
	public static double[] generateSignal() {
		Random r = new Random();
		DecimalFormat df = new DecimalFormat("#.000000");
		int numHarmonics = 5;
		double signalLength = 1.0;
		int sampleRate = 44100;
		double[] generatedSamples;
		
		float[] signals = new float[numHarmonics];
		
		for(int i = 0; i < numHarmonics; i++)
			signals[i] = r.nextInt(22050);
		
		int sampleNum =   (int)signalLength * sampleRate;
		
		generatedSamples = new double[(int)sampleNum];
		
		for(int i = 0; i < sampleNum; i++) {
			double s[] = new double[numHarmonics];
			
			for(int j = 0; j < numHarmonics; j++) {
				s[j] = Math.sin(2.0 * signals[j] * Math.PI * (i / signalLength));
				s[j] = Double.parseDouble(df.format(s[j]));
				generatedSamples[i] += s[j];
			}
		}
		
		return generatedSamples;
	}

}
