package Indexing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class rank{
	
	int score;
	String postl;
}

class Comp implements Comparator<rank> 
{ 
    @Override
    public int compare(rank x, rank y) 
    { 
        return (y.score-x.score);
    } 
}

public class Ranking {
	
	public static String sortByRank(String postl){
		
		StringBuilder str = new StringBuilder();
		str.setLength(0);

		ArrayList<rank> rmap = new ArrayList<>();		
		String[] docs=postl.split(";");

		for(int i=0;i<docs.length;i++){
			
			int body=0,title=0,ext=0,ref=0,cat=0,info=0;
			String temp="";
			StringBuilder freq = new StringBuilder();
			int score=0;

			if(docs[i].split("-").length>0)
				temp=docs[i].split("-")[1];

			int j=0,n=temp.length();
			
			while(j<n){
				
				if(temp.charAt(j)=='T'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					title=Integer.parseInt(freq.toString());
					score+=1000*title;
				}
				else if(temp.charAt(j)=='B'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					body=Integer.parseInt(freq.toString());
					score+=2*body;
				}
				else if(temp.charAt(j)=='C'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					cat=Integer.parseInt(freq.toString());
					score+=20*cat;
				}
				else if(temp.charAt(j)=='I'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					info=Integer.parseInt(freq.toString());
					score+=25*info;
				}
				else if(temp.charAt(j)=='E'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					ext=Integer.parseInt(freq.toString());
					score+=ext;
				}
				else if(temp.charAt(j)=='R'){
					
					j++;
					freq.setLength(0);
					while(j<n && temp.charAt(j)>='0' && temp.charAt(j)<='9'){
						
						freq.append(temp.charAt(j));
						j++;
					}
					ref=Integer.parseInt(freq.toString());
					score+=ref;
				}
			}

			rank r = new rank();
			r.score=score;
			r.postl=docs[i].split("-")[0]+"-"+temp;
			rmap.add(r);
		}
		
		Collections.sort(rmap,new Comp());
	
		for (int i=0;i<rmap.size();i++){

			str.append(rmap.get(i).postl);
			str.append(";");
		}
	
		return str.toString();
	}
}
