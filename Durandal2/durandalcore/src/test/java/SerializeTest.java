package tk.icudi.durandal.test;

import java.io.IOException;

import junit.framework.Assert;
import tk.icudi.durandal.core.logic.Monster;
import tk.icudi.durandal.core.logic.Serializer;
import tk.icudi.durandal.core.logic.ShortMessage;
import tk.icudi.durandal.core.logic.ShortMessage.Identifier;

public class SerializeTest {

	public void testSerializeResetGame() throws Exception {
		ShortMessage sourceMsg = new ShortMessage(Identifier.resetGame, null);
		String test = "Obects have to be the same after operation";
		Assert.assertEquals(test, sourceMsg, simulateTransport(sourceMsg));
	}
	
	public void testSerializeBuildCreep() throws Exception{
		Monster creep = new Monster();
		creep.setLifepoints(100);
		ShortMessage sourceMsg = new ShortMessage(ShortMessage.Identifier.build_creep, creep);
			
		String test = sourceMsg.identifier + " messages have to be the same after operation";
		Assert.assertEquals(test, sourceMsg.object, simulateTransport(sourceMsg).object);
	}
	
	private ShortMessage simulateTransport(ShortMessage sourceMsg) throws IOException{
		
		String transportString =  Serializer.parcelableToString(sourceMsg);
		
		return Serializer.parcelableFromString(ShortMessage.class, transportString);
	}
	
	public void testPacelableIsBetter() throws Exception {
		Monster creep = new Monster();
		creep.setLifepoints(100);
		ShortMessage sourceMsg = new ShortMessage(ShortMessage.Identifier.build_creep, creep);
		
		String transportStringParcelable = Serializer.parcelableToString(sourceMsg);
		String transportStringSerializable = Serializer.serializableToString(sourceMsg);
		
		// 10-13 12:42:35.610: 693 => 487
		// 10-13 12:57:44.450: 722 => 495
		// 10-13 13:07:44.450: 706 => 288
		// 11-02             : 706 => 288
		
		String test = "Parcelable should make the transport Strings smaller " + transportStringSerializable.length() + " => " + transportStringParcelable.length();
		System.out.println(" --- test: " + test);
		
		Assert.assertTrue(test, transportStringSerializable.length() > transportStringParcelable.length());
	}

}
