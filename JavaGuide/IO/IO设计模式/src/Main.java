import java.util.*;

public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        HashMap<Character,Integer> map=new HashMap<>();
        for(int i=0;i<str.length();i++){
            Integer tmp=map.get(str.charAt(i));
            if(tmp==null){
                map.put(str.charAt(i),1);
            }
            else{
                map.put(str.charAt(i),tmp+1);
            }
        }
        System.out.println(getString(map));


    }

    public static StringBuilder getString(Map<Character,Integer> map){
        List<Map.Entry<Character,Integer>> entryList=new ArrayList<>(map.entrySet());
        Collections.sort(entryList,new Comparator<Map.Entry<Character,Integer>>(){
            @Override
            public int compare(Map.Entry<Character,Integer> o1,Map.Entry<Character,Integer> o2){
                if(o1.getValue()-o2.getValue()<0){
                    return -1;
                }
                else if(o1.getValue()-o2.getValue()>0){
                    return 1;
                }
                else{
                    return o1.getKey()-o2.getKey();
                }
            }
        });
        StringBuilder stringBuilder=new StringBuilder();
        for(Map.Entry<Character,Integer> e:entryList){
            stringBuilder.append(e.getKey());
        }
        return stringBuilder;
    }

}