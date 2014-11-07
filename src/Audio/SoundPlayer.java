package Audio;

import java.io.*;
import javax.sound.sampled.*;


public class SoundPlayer {



	//public AudioInputStream ais;
	public AudioFormat format;
	private byte[] samples;
	//public String filepath;
	
	public SoundPlayer(String filename){
		//this.filepath = filename;
		try{
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
			//ais = stream;
			format = stream.getFormat();
			samples = getSamples(stream);
		}
		catch (UnsupportedAudioFileException ex){
			ex.printStackTrace();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void playSound(String filename) {
		SoundPlayer sound = new SoundPlayer(filename);
		InputStream stream = new ByteArrayInputStream(sound.getSamples());
		sound.play(stream);
	}
	
	public byte[] getSamples(){
		return samples;
	}
	
	public byte[] getSamples(AudioInputStream audioStream){
		int length = (int)(audioStream.getFrameLength() * format.getFrameSize());
		byte[] samples = new byte[length];
		DataInputStream is = new DataInputStream(audioStream);
		try{
			is.readFully(samples);
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		
		return samples;
	}
	
	public void play(InputStream source){ //InputStream source

		//InputStream source = new ByteArrayInputStream(samp);

		int bufferSize = format.getFrameSize()*Math.round(format.getSampleRate()/10);
		byte[] buffer = new byte[bufferSize];
		SourceDataLine line;



		try {
			DataLine.Info info = 
					new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(format,bufferSize);
		}
		catch (LineUnavailableException ex){
			System.out.println("Line Unavailable Exception in play(InputStream source)");
			ex.printStackTrace();
			return;
			
		}
		
		line.start();
		try{
			int numBytesRead= 0;
			while(numBytesRead != -1){
				numBytesRead = source.read(buffer,0, buffer.length);
				if (numBytesRead != -1){
					line.write(buffer,0,numBytesRead);
				}
			}
		}
		catch (Exception ex) {
			System.out.println("Exception thrown in play(InputStream source)");
			ex.printStackTrace();
		}
		//wait until all data is played
		line.drain();
		//close the line
		line.close();

		
	}


}
