package tk.icudi.durandal.core.logic;


public class NameGenerator {

	private static String fencingSyllables[] = { "ro", "land", "dah", "lem", "les", "lie", "ned", "wed", "fa", "bi", "an", "geng", "na", "gel"};
	
	public static String getName(){
		return getName(randomInt(2,3));
	}
	
	public static String getName(int syllables){
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<syllables; i++){
			appendRandomSyllable(builder);
		}
		return builder.toString();
	}


	private static void appendRandomSyllable(StringBuilder builder) {
		int chance = randomInt(0, fencingSyllables.length-1);
		builder.append(fencingSyllables[chance]);
	}

	private static int randomInt(int min, int max) {
		return (int) (min + (Math.random() * (max + 1 - min)));
	}

}
