package wave_file_reader_from_web;

import java.io.*;
import java.util.ArrayList;

import model.ResultDFT;

public class ReadExample
{
	public ReadExample(File fileToRead)
	{
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(fileToRead);

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			double[] buffer = new double[300 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			
			ArrayList<ResultDFT> results = new ArrayList<>();
			double[] real, img, outReal, outImg;
			
			

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 300);

//				System.out.println(framesRead * numChannels);
				img = new double[framesRead * numChannels];
				real = new double[framesRead * numChannels];
				outReal = new double[framesRead * numChannels];
				outImg = new double[framesRead * numChannels];
				for(int i = 0; i<framesRead * numChannels; i++){
					img[i]=0;
				}
				
				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
//					System.out.println(buffer[s]);
					real[s] = buffer[s];
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
				computeDft(real, img, outReal, outImg);
				results.add(new ResultDFT(outReal, outImg));
//				System.out.println(outReal[0]);
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
			
//			for(ResultDFT r : results){
//				System.out.println(r.getReal()[0]);
//			}
			
//			for(int i=0; i<results.get(0).getReal().length; i++){
//				System.out.println("REALNI DEO: "+results.get(0).getReal()[i]+" ; IMAGINARNI DEO: "+results.get(0).getImg()[i]);
//			}
//			for(int i=0; i<results.get(results.size()-1).getReal().length; i++){
//				System.out.println("REALNI DEO: "+results.get(results.size()-1).getReal()[i]+" ; IMAGINARNI DEO: "+results.get(results.size()-1).getImg()[i]);
//			}
//			
//			System.out.println("PROVERA DA LI??");
//			System.out.println(results.get(0).getReal().length);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	public static void computeDft(double[] inreal, double[] inimag, double[] outreal, double[] outimag) {
		int n = inreal.length;
		for (int k = 0; k < n; k++) {  // For each output element
			double sumreal = 0;
			double sumimag = 0;
			for (int t = 0; t < n; t++) {  // For each input element
				double angle = 2 * Math.PI * t * k / n;
				sumreal +=  inreal[t] * Math.cos(angle) + inimag[t] * Math.sin(angle);
				sumimag += -inreal[t] * Math.sin(angle) + inimag[t] * Math.cos(angle);
			}
			outreal[k] = sumreal;
			outimag[k] = sumimag;
		}
	}
}
