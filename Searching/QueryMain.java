// package Searching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class QueryMain {

	public static HashSet<String> stopWords = new HashSet<String>();
    public static String stop_words[]= {"coord","gr","com","tr","td","nbsp","http","https","www","a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","reflist","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
    public static List<list> tmap = new ArrayList<list>();
    public static List<Titlelist> titleIndex = new ArrayList<Titlelist>();
    
	public static void main(String[] args) {
		
		
		for(int i=0;i<437;i++)
	 		stopWords.add(stop_words[i]);
		
		try{
			/*
			 * tertiary
			 */			
			BufferedReader br = new BufferedReader(new FileReader("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/TertiaryIndex"));
			
			String line = br.readLine();
			
	        while(line!=null){   
	             String term = line.substring(0,line.indexOf("="));
	             long indx = Long.parseLong(line.substring(line.indexOf("=")+1));
	             list l = new list(term, indx);
	             tmap.add(l);
				line = br.readLine();	
	        }
	        br.close();
			
	        /*
	         * secondary title
	         */
	        BufferedReader br1 = new BufferedReader(new FileReader("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/SecondaryTitleIndex"));
	        line = br1.readLine();
	        while(line!=null){
	             String docId = line.substring(0,line.indexOf(":"));
	             Long offset = Long.parseLong(line.substring(line.indexOf(":")+1));
	             Titlelist t = new Titlelist(Long.parseLong(docId), offset);
	             titleIndex.add(t);
	             line = br1.readLine();
	        }
	        br1.close();
	       
			Scanner kb = new Scanner(System.in);
			while (true){	
				String query=kb.nextLine();
				QueryParser qp = new QueryParser();
				long start  = System.currentTimeMillis();
	        	query=query.replaceAll("[!@#$%+^&;*'.><]", "");
				qp.Parser(query);
				System.out.println("Response time : "+(System.currentTimeMillis()-start));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
