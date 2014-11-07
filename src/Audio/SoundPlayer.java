package Audio;

import java.io.*;
import javax.sound.sampled.*;


public class SoundPlayer {
	public static void main(String[] args) {
		SoundPlayer sound = new SoundPlayer("./src/Audio/jumpsound.aiff");
		InputStream stream = new ByteArrayInputStream(sound.getSamples());
		sound.play(stream);
	}


	
	public AudioFormat format;
	private byte[] samples;
	
	public SoundPlayer(String filename){
		try{
		//	AudioInputStream stream = AudioSystem.get(getClass().getResourceAsStream(filename));
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
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
	
	public void play(InputStream source){
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
		catch (IOException ex) {
			ex.printStackTrace();
		}
		//wait until all data is played
		line.drain();
		//close the line
		line.close();

		
	}


}
