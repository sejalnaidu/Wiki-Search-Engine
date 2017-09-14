// package Searching;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import Searching.Stemming;

class list{
	
	String term;
	Long index;
	public list(String term, Long l) {
		this.term = term;
		this.index = l;
	}
}

class Termlist{
	
	String term;
	String postl;
	public Termlist(String t, String po) {
		// TODO Auto-generated constructor stub
		this.postl = po;
		this.term = t;
	}
}

class Titlelist{
	
	Long Id;
	Long score;
	
	public Titlelist(Long i, Long s){
		this.Id = i;
		this.score = s;
		
	}
}

public class QueryParser {
	
	public List<list> smap = new ArrayList<list>();
    public HashMap<Long,Long> map = new HashMap<Long,Long>();
	
	public void QueryHandler(String str,char field){
		
		list tl = new list(str, 0L);
		String line;
		
		Comparator<list> c = new Comparator<list>() {
		      public int compare(list l1, list l2) 
		      {
		        return l1.term.compareTo(l2.term);
		      }
		};
		
		int tindex = Collections.binarySearch(QueryMain.tmap,tl,c);
		long tstart;
		if(tindex<0){
			tindex*=-1;
			tstart = QueryMain.tmap.get(tindex-2).index;
		}
		else if(tindex>3)
			tstart= QueryMain.tmap.get(tindex-3).index;
		else
			tstart=0;
		
		try{

		  RandomAccessFile sfile = new RandomAccessFile("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/SecondaryIndex", "r");
		  sfile.seek(tstart);
		  
		  line=sfile.readLine();
		  for(int j=0;j<220 && line!=null;j++){

			 long indx = Long.parseLong(line.substring(line.indexOf("=")+1));
	         list l = new list(line.substring(0,line.indexOf("=")), indx);
	         smap.add(l);
		     line = sfile.readLine();
		   }
	  
		    sfile.close();
		  
		    list sl = new list(str, 0L);
			
			int sindex = Collections.binarySearch(smap,sl,c);
			long sstart;
		
			if(sindex<0){
				
				sindex*=-1;
				sstart = smap.get(sindex-2).index;
			}
			else if(sindex>3)
				sstart= smap.get(sindex-3).index;
			else
				sstart=0;
	
			smap.clear();
			
			RandomAccessFile file = new RandomAccessFile("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/PrimaryIndex", "r");
			file.seek(sstart);
			
			line=file.readLine();		
			for(int j=0;j<220 && line!=null;j++){
				
				String word=line.substring(0,line.indexOf("="));
				if(word.equals(str)){

					String postl = line.substring(line.indexOf("=")+1);
					String[] docs = postl.split(";");

					for(int k=0;k<docs.length;k++){
						
						int body=0,title=0,ext=0,ref=0,cat=0,info=0;
						String temp="";
						StringBuilder freq = new StringBuilder();
						Long score=0L;
						Long id = Long.parseLong(docs[k].substring(0,docs[k].indexOf("-")));
						temp=docs[k].substring(docs[k].indexOf("-")+1);
		
						int len=0,n=temp.length(),fw;
						
						while(len<n){
							
							if(temp.charAt(len)=='T'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								title=Integer.parseInt(freq.toString());
								if(field=='T'){
									fw=1000*1000;
									score+=1000*title*fw;
								}
								else
									score+=1000*title*1000;
							}
							else if(temp.charAt(len)=='B'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								body=Integer.parseInt(freq.toString());
								if(field=='B'){
									fw=2*1000;
									score+=2*body*fw;
								}
								else
									score+=2*body*2;
							}
							else if(temp.charAt(len)=='C'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								cat=Integer.parseInt(freq.toString());
								if(field=='C'){
									fw=30*1000;
									score+=20*cat*fw;
								}
								else
									score+=20*cat*30;
							}
							else if(temp.charAt(len)=='I'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								info=Integer.parseInt(freq.toString());
								if(field=='I'){
									fw=25*1000;
									score+=25*info*fw;
								}
								else
									score+=25*info*25;
							}
							else if(temp.charAt(len)=='E'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								ext=Integer.parseInt(freq.toString());
								if(field=='E'){
									fw=1*1000;
									score+=ext*fw;
								}
								else
									score+=ext;
							}
							else if(temp.charAt(len)=='R'){
								
								len++;
								freq.setLength(0);
								while(len<n && temp.charAt(len)>='0' && temp.charAt(len)<='9'){
									
									freq.append(temp.charAt(len));
									len++;
								}
								ref=Integer.parseInt(freq.toString());
								if(field=='R'){
									fw=1*1000;
									score+=ref*fw;
								}
								else
									score+=ref;
							}
						}

						//double postlN = (double)17640866/docs.length;
						score = (long)Math.log10(score); 
						map.put(id, map.getOrDefault(id,  (long) 0) + score);		
					}
					break;
				}
				line=file.readLine();
			}
			
			file.close();
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	}
	
	public void Parser(String query){
		
		map.clear();
		List<String> topTitles = new ArrayList<String>();
	    
		String[] q=query.split(" ");

		try{
			
	        char field='\0';
	        boolean flag = false;
			for(int i=0;i<q.length;i++){
				
				if(q[i].contains("B:")){
					field='B';
					
				}else if(q[i].contains("I:")){
					field='I';
				}else if(q[i].contains("T:")){
					field='T';
				}else if(q[i].contains("C:")){
					field='C';
				}else if(q[i].contains("E:")){
					field='E';
				}else if(q[i].contains("R:")){
					field='R';
				}
				else
					flag=true;
				
				if(!flag)
					q[i]=parse(q[i].substring(2));
				else
					q[i]=parse(q[i]);

				if(q[i]!="")
					QueryHandler(q[i],field);
			}
		    
			Set<Entry<Long, Long>> set = map.entrySet();
	        List<Entry<Long, Long>> list = new ArrayList<Entry<Long, Long>>(set);
	        Collections.sort( list, new Comparator<HashMap.Entry<Long, Long>>()
	        {
	            public int compare( HashMap.Entry<Long, Long> o1, HashMap.Entry<Long, Long> o2 )
	            {
	                return (o2.getValue()).compareTo( o1.getValue() );
	            }
	        });

	        Comparator<Titlelist> c1;
	         c1 = new Comparator<Titlelist>() 
	         {
	             public int compare(Titlelist t1, Titlelist t2)
	             {
	                 return (int)(t1.Id-t2.Id);
	             }
	         };
	         
	         RandomAccessFile ifile = new RandomAccessFile("/home/sejal/Sem3/IRE/Mini-Project/Outputs/out/PrimaryTitleIndex", "r");

	         for(int j=0; j<10 && j<list.size(); j++)
	         {
	             Long docId =list.get(j).getKey();
	             Titlelist t = new Titlelist(docId, 0L);
	             
	             int index = Collections.binarySearch(QueryMain.titleIndex,t,c1);
	            
	             long istart = 0;
	             if(index<0)
	             {
	                 index*=-1;
	                 if(index>2)
	                     istart = QueryMain.titleIndex.get(index-2).score;
	                 else
	                     istart=0;
	             }
	             else if(index>1)
	            	 istart=QueryMain.titleIndex.get(index-2).score;
	             
	             ifile.seek(istart);
	             String line=ifile.readLine();
	             
	             for(int k=0;k<220 && line!=null;k++){
	            	 
            		 Long id=Long.parseLong(line.substring(0,line.indexOf(":")));
            		 String ttl=line.substring(line.indexOf(":")+1);
	            	 if(id.longValue()==docId.longValue())
	            		 topTitles.add(ttl);
	            	 line=ifile.readLine();
	             }
	         }		
	         ifile.close();

			for(int k=0;k<topTitles.size() && k<10;k++)
	        	System.out.println(topTitles.get(k));		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public String parse(String str){
		 
	    str=str.toLowerCase();
    	str.trim();

		if(str.length()<=2||str.isEmpty())
			return "";
				
		if(QueryMain.stopWords.contains(str))
			return "";

    	Stemming s = new Stemming();
		s.add(str.toCharArray(),str.length());
		str=s.stem();
		
		return str;
	}
}
