/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recommendationsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Bhailoo
 */
public class RecommendationSystem {

     public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
         
         
         /*Initialization of variables  */
         
        Scanner sc=new Scanner(System.in);;
        BufferedReader reader = null;
        String line=null;
        Scanner scanner = null;
        String[] userid = new String[total_users.total_users];
        float[][] userprofile=new float[total_users.total_users][total_users.total_attributes];
        int i=0;
        int user_id=0;
        int attribute = 0;
        
        //Initialization Complete

        //Reading Of userprofile
        
        
        
        reader = new BufferedReader(new FileReader("src/consumerprofile.csv"));
        line=reader.readLine();
        line=reader.readLine();
        while(line!=null)
         {                   
             scanner = new Scanner(line);
                scanner.useDelimiter(",");
                userid[i]=scanner.next();
                attribute=0;
                
                while(attribute<total_users.total_attributes-2)
                {
                    userprofile[user_id][attribute]=Float.parseFloat(scanner.next());
                    attribute++;
                }
                
                user_id++;
                i++;
                line=reader.readLine();
                
                
         }
        System.out.println("UserProfile read successfully!");
        System.out.println();
        System.out.println();
        //uerprofile read !!
         //    double [][]user_parking_corelation=corelate_user_place(userprofile);
         
        //Reading Of PlaceFile
        
         reader = new BufferedReader(new FileReader("src/geoplace.csv"));
         line=reader.readLine();
         
         line=reader.readLine();
          attribute=0;
         int placeid[]=new int[130];
         double [][] place_info=new double[total_users.total_places][total_users.total_attributes_in_geoplaces-1];
         String [] place_names=new String[total_users.total_places];
          i=0;
         int place_id=0;
         while(line!=null)
         {
             
                           
             scanner = new Scanner(line);
                scanner.useDelimiter(",");
                placeid[i++]=Integer.parseInt(scanner.next());
                        attribute=0;
                
                while(attribute<total_users.total_attributes_in_geoplaces-1)
                {
                    place_info[place_id][attribute++]=Double.parseDouble(scanner.next());
                }
                place_names[place_id++]=scanner.next();
                
                
                line=reader.readLine();
                
                
         }
        System.out.println("places read successfully!"); 
         //PlaceInformation Read!!
        
                double [][]normalized_node_value_place_per_user=corelate_user_place(userprofile,place_info,placeid); 
                double [][]ratings_of_user=get_ratings();
               // System.out.println(userprofile[user_id-1][attribute-1]);
                double [][]personal_info=count_personal_info_distance(userprofile);
                double [][]taste_info=count_taste_info_distance(userprofile);
                double [][]payment_info=count_payment_info_distance(userprofile);
                double [][]normalized_distance=count_normalized_distance(personal_info,taste_info,payment_info);
                double [][]mapping_matrix=map_place_ratings(normalized_node_value_place_per_user,ratings_of_user);
                double [][]modified_ratings=increase_ratings(ratings_of_user,normalized_node_value_place_per_user,normalized_distance);
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("To which User do you want to recommend the restaurant?");
                int user_number=0;
                user_number=sc.nextInt();
                get_recommendations(user_number,modified_ratings,placeid,place_names);
//                double [][]mapping_with_non_common=map_with_noncommon(normalized_node_value_place_per_user,ratings_of_user);
                //System.out.println(normalized_distance[total_users.total_users-79][total_users.total_users-1]);
                    
    }

    
    private static double[][] count_personal_info_distance(float[][] userprofile) {
        double distance[][];   
        distance = new double[total_users.total_users][total_users.total_users];
        for(int user_id=0;user_id<total_users.total_users;user_id++)
           {
               for(int target_id=0;target_id<total_users.total_users;target_id++)
               {
                   distance[user_id][target_id]=0;
                   /*if(target_id==user_id)
                   {
                       distance[user_id][target_id]=0;
                   }
                   else if(target_id<user_id)
                   {
                       distance[user_id][target_id]=distance[target_id][user_id];
                   }*/
                   for(int attribute=0;attribute<15;attribute++)
                    {
                      distance[user_id][target_id]+=((userprofile[user_id][attribute]-userprofile[target_id][attribute])*(userprofile[user_id][attribute]-userprofile[target_id][attribute]));
                      
                    }
                   distance[user_id][target_id]/=7.5;
                   
               }
           }
            return distance;
        
    }
        private static double[][] count_taste_info_distance(float[][] userprofile) {
        double jaccard_distance[][]=new double[total_users.total_users][total_users.total_users];   
        for(int user_id=0;user_id<total_users.total_users;user_id++)
           {
               for(int target_id=0;target_id<total_users.total_users;target_id++)
               {
                   jaccard_distance[user_id][target_id]=0;
                   /*if(target_id==user_id)
                   {
                       distance[user_id][target_id]=0;
                   }
                   else if(target_id<user_id)
                   {
                       distance[user_id][target_id]=distance[target_id][user_id];
                   }*/
                   for(int attribute=15;attribute<119;attribute++)
                    {
                        if(userprofile[user_id][attribute]==userprofile[target_id][attribute])
                      jaccard_distance[user_id][target_id]+=1;
                    }
                   jaccard_distance[user_id][target_id]/=2;
               }
           }
            return jaccard_distance;
        
    }    
        
            
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    

    private static double[][] count_payment_info_distance(float[][] userprofile) {
        
        double jaccard_distance[][]=new double[total_users.total_users][total_users.total_users];   
        for(int user_id=0;user_id<total_users.total_users;user_id++)
           {
               for(int target_id=0;target_id<total_users.total_users;target_id++)
               {
                   jaccard_distance[user_id][target_id]=0;
                   /*if(target_id==user_id)
                   {
                       distance[user_id][target_id]=0;
                   }
                   else if(target_id<user_id)
                   {
                       distance[user_id][target_id]=distance[target_id][user_id];
                   }*/
                   for(int attribute=119;attribute<total_users.total_attributes;attribute++)
                    {
                        if(userprofile[user_id][attribute]==userprofile[target_id][attribute])
                      jaccard_distance[user_id][target_id]+=1;
                    }
                   jaccard_distance[user_id][target_id]*=20;
                   
               }
           }
            return jaccard_distance;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private static double[][] count_normalized_distance(double[][] personal_info, double[][] taste_info, double[][] payment_info) throws IOException {
        
        double normalized_distance[][]=new double[total_users.total_users][total_users.total_users];   
        for(int user_id=0;user_id<total_users.total_users;user_id++)
           {
               for(int target_id=0;target_id<total_users.total_users;target_id++)
               {
                   normalized_distance[user_id][target_id]=0;
                   /*if(target_id==user_id)
                   {
                       distance[user_id][target_id]=0;
                   }
                   else if(target_id<user_id)
                   {
                       distance[user_id][target_id]=ormalized_distancedistance[target_id][user_id];
                   }*/
                   normalized_distance[user_id][target_id]+=personal_info[user_id][target_id];
                   normalized_distance[user_id][target_id]+=taste_info[user_id][target_id];
                   normalized_distance[user_id][target_id]+=payment_info[user_id][target_id];
               }
           }
        try (BufferedWriter br = new BufferedWriter(new FileWriter("users.csv"))) {
            StringBuilder sb = new StringBuilder();
            int user_id=0;
            for (user_id=0;user_id<138;user_id++) {
                sb.append(String.valueOf(normalized_distance[user_id][0]));
                for(int p=1;p<138;p++)
                {
                    sb.append(",");
                    sb.append(String.valueOf(normalized_distance[user_id][p]));
                
                }
                sb.append("\n");
            }
            
            br.write(sb.toString());
        }
//       
            return normalized_distance;
        
        
        
     
//   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    /**
     * @param args the command line arguments
     */
   

    private static double[][] corelate_user_place(float[][] userprofile,double[][]place_info,int[]placeid) throws FileNotFoundException, IOException {
        double distance_between_users_places[][]=new double[138][130];
        int user_id=0;
        int place_id=0;
        BufferedReader reader ;
        String line=null;
        Scanner scanner = null;
        int i=0;
        user_id=0;
        int attribute = 0;
       
        
         reader = new BufferedReader(new FileReader("src/cuisine_intersects_rating.csv"));
         line=reader.readLine();
         
         line=reader.readLine();
          attribute=0;
         place_id=0;
         
         int [][] place_cuisine_intersection=new int[total_users.total_places][2];
         while(line!=null)
         {
             
                           
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                place_cuisine_intersection[i][0]=Integer.parseInt(scanner.next());
                place_cuisine_intersection[i++][1]=Integer.parseInt(scanner.next());
                line=reader.readLine();
                
                
         }
         int place_cuisine_info[]=new int[total_users.total_places];
         for( i=0;i<total_users.total_places;i++)
         {
            for(int j=0;j<i+1;j++)
            {
                if(placeid[i]==place_cuisine_intersection[j][0] && place_cuisine_intersection[j][1]>1)
                {
                        place_cuisine_info[i]=place_cuisine_intersection[j][1];
                        
                        break;
                }
                else
                {
                    place_cuisine_info[i]=1;
                }
            }
         }
         for( i=0;i<total_users.total_places;i++)
         {
            for(int j=0;j<i+1;j++)
            {
                if(placeid[i]==place_cuisine_intersection[j][0] && place_cuisine_intersection[j][1]>1)
                {
                        place_cuisine_info[i]=place_cuisine_intersection[j][1];
                        break;
                }
                else
                {
                    place_cuisine_info[i]=1;
                }
            }
         }
        
         reader = new BufferedReader(new FileReader("src/paymentplace_intersects_ratings.csv"));
         
          attribute=0;
         place_id=0;
         int []places_in_payment_place_intersection=new int[total_users.total_places];
         float [][] payment_place=new float[total_users.total_places][5];
         float [][]payment_place_rating_intersection=new float[115][5];
         
         
         line=reader.readLine();
         int d=0;
         while(line!=null)
         {
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                places_in_payment_place_intersection[place_id]=Integer.parseInt(scanner.next());
                int k=0;
                
                while(k<5)
                {
                   
                    payment_place_rating_intersection[place_id][k]=Float.parseFloat(scanner.next());
                    k++;
                }
                place_id++;
                line=reader.readLine();
          }
         try (BufferedWriter br = new BufferedWriter(new FileWriter("users_places.csv"))) {
            StringBuilder sb = new StringBuilder();
            
            for (place_id=0;place_id<115;place_id++) {
                sb.append(String.valueOf(  payment_place_rating_intersection[place_id][0]));
                for(int p=1;p<5;p++)
                {
                    sb.append(",");
                    sb.append(String.valueOf(payment_place_rating_intersection[place_id][p]));
                
                }
                sb.append("\n");
            }
            
            br.write(sb.toString());
        }
        
         int payment_place_info[]=new int[1];
         for( i=0;i<total_users.total_places;i++)
         {
            for(int j=0;j<i+1;j++)
            {
                if(placeid[i]==places_in_payment_place_intersection[j])
                {
                        for(int p=0;p<5;p++)
                        {
                            payment_place[i][p]=payment_place_rating_intersection[j][p];
                        }
                        break;
                }
                else
                {
                    for(int p=0;p<5;p++)
                        {
                            payment_place[i][p]=5;
                        }
                }
            }
         }
         try (BufferedWriter br = new BufferedWriter(new FileWriter("places.csv"))) {
            StringBuilder sb = new StringBuilder();
            
            for (place_id=0;place_id<130;place_id++) {
                sb.append(String.valueOf( payment_place[place_id][0]));
                for(int p=1;p<5;p++)
                {
                    sb.append(",");
                    sb.append(String.valueOf(payment_place[place_id][p]));
                
                }
                sb.append("\n");
            }
            
            br.write(sb.toString());
        }
         
        //System.out.println(payment_place[1][1]);
        for(user_id=0;user_id<138;user_id++)
        {
            for(place_id=0;place_id<130;place_id++)
            {
                /*Common Attributes between users and places*/
                distance_between_users_places[user_id][place_id]=0;
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][0]-place_info[place_id][0])*(userprofile[user_id][0]-place_info[place_id][0]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][1]-place_info[place_id][1])*(userprofile[user_id][1]-place_info[place_id][1]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][2]-place_info[place_id][3])*(userprofile[user_id][2]-place_info[place_id][3]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][3]-place_info[place_id][2])*(userprofile[user_id][3]-place_info[place_id][2]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][4]-place_info[place_id][4])*(userprofile[user_id][4]-place_info[place_id][4]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][5]-place_info[place_id][7])*(userprofile[user_id][5]-place_info[place_id][7]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][11]-place_info[place_id][9])*(userprofile[user_id][11]-place_info[place_id][9]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][11]-place_info[place_id][10])*(userprofile[user_id][11]-place_info[place_id][10]);
                 distance_between_users_places[user_id][place_id]+=(userprofile[user_id][13]-place_info[place_id][6])*(userprofile[user_id][13]-place_info[place_id][6]);
                 distance_between_users_places[user_id][place_id]*=1.11;
              
                /*Payment attribute weight            */
              
               distance_between_users_places[user_id][place_id]+=(userprofile[user_id][121]-payment_place[place_id][1])*(userprofile[user_id][121]-payment_place[place_id][1]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][119]-payment_place[place_id][0])*(userprofile[user_id][119]-payment_place[place_id][0]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][120]-payment_place[place_id][4])*(userprofile[user_id][120]-payment_place[place_id][4]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][122]-payment_place[place_id][2])*(userprofile[user_id][122]-payment_place[place_id][2]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][123]-payment_place[place_id][3])*(userprofile[user_id][123]-payment_place[place_id][3]);
                
             /*   Not Common attributes in place*/
               distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][5];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][6];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][8];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][11];
                
                
               
               /* cuisine weight*/
                distance_between_users_places[user_id][place_id]+=100*place_cuisine_info[place_id];
                   
            }
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter("placedistance_with_attributes.csv"))) {
            StringBuilder sb = new StringBuilder();
            
            for (place_id=0;place_id<138;place_id++) {
                sb.append(String.valueOf(distance_between_users_places[place_id][0]));
                for(int p=1;p<130;p++)
                {
                    sb.append(",");
                    sb.append(String.valueOf(distance_between_users_places[place_id][p]));
                
                }
                sb.append("\n");
            }
            
            br.write(sb.toString());
        }
        for(user_id=0;user_id<138;user_id++)
        {
            for(place_id=0;place_id<130;place_id++)
            {
                /*Common Attributes between users and places*/
                distance_between_users_places[user_id][place_id]=0;
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][0]-place_info[place_id][0])*(userprofile[user_id][0]-place_info[place_id][0]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][1]-place_info[place_id][1])*(userprofile[user_id][1]-place_info[place_id][1]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][2]-place_info[place_id][3])*(userprofile[user_id][2]-place_info[place_id][3]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][3]-place_info[place_id][2])*(userprofile[user_id][3]-place_info[place_id][2]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][4]-place_info[place_id][4])*(userprofile[user_id][4]-place_info[place_id][4]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][5]-place_info[place_id][7])*(userprofile[user_id][5]-place_info[place_id][7]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][11]-place_info[place_id][9])*(userprofile[user_id][11]-place_info[place_id][9]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][11]-place_info[place_id][10])*(userprofile[user_id][11]-place_info[place_id][10]);
                 distance_between_users_places[user_id][place_id]+=(userprofile[user_id][13]-place_info[place_id][6])*(userprofile[user_id][13]-place_info[place_id][6]);
                 distance_between_users_places[user_id][place_id]*=1.11;
              
                /*Payment attribute weight            */
             /*  
               distance_between_users_places[user_id][place_id]+=(userprofile[user_id][121]-payment_place[place_id][1])*(userprofile[user_id][121]-payment_place[place_id][1]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][119]-payment_place[place_id][0])*(userprofile[user_id][119]-payment_place[place_id][0]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][120]-payment_place[place_id][4])*(userprofile[user_id][120]-payment_place[place_id][4]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][122]-payment_place[place_id][2])*(userprofile[user_id][122]-payment_place[place_id][2]);
                distance_between_users_places[user_id][place_id]+=(userprofile[user_id][123]-payment_place[place_id][3])*(userprofile[user_id][123]-payment_place[place_id][3]);
                
                /*Not Common attributes in place
               distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][5];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][6];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][8];
                distance_between_users_places[user_id][place_id]+=12.5*place_info[place_id][11];
                
                
                /*cuisine weight
                distance_between_users_places[user_id][place_id]+=100*place_cuisine_info[place_id];
                   */
            }
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter("user_place_distance.csv"))) {
            StringBuilder sb = new StringBuilder();
            
            for (place_id=0;place_id<138;place_id++) {
                sb.append(String.valueOf(distance_between_users_places[place_id][0]));
                for(int p=1;p<130;p++)
                {
                    sb.append(",");
                    sb.append(String.valueOf(distance_between_users_places[place_id][p]));
                
                }
                sb.append("\n");
            }
            
            br.write(sb.toString());
        }
         
        //System.out.println(distance_between_users_places[0][0]);
        return distance_between_users_places;
    }

    private static double[][] get_ratings() throws FileNotFoundException, IOException {
        int user_id=0;
        int place_id=0;
        BufferedReader reader ;
        String line=null;
        Scanner scanner = null;
        int i=0;
        
        
       
        double ratings[][]=new double[total_users.total_users][total_users.total_places];
        String users[]=new String[total_users.total_users];
         reader = new BufferedReader(new FileReader("src/ratings_only.csv"));
         line=reader.readLine();
         
         line=reader.readLine();
        while(line!=null)
         {
                place_id=0;
                scanner = new Scanner(line);
                scanner.useDelimiter(",");
                users[user_id]=scanner.next();
                while(place_id<total_users.total_places)
                    {
                        
                        
                        
                        
                    double rate=Double.parseDouble(scanner.next());
                    
                             ratings[user_id][place_id]=rate;
                    
                    
                    place_id++;
                }
                
                 line=reader.readLine();
                 
                user_id++;
                }
               
        try (BufferedWriter br = new BufferedWriter(new FileWriter("rating.csv"))) {
            StringBuilder sb = new StringBuilder();
        int len;
           for(int user=0;user<total_users.total_users;user++)
           {
               sb.append(String.valueOf(ratings[user][0]));
               for (place_id=0;place_id<total_users.total_places-1;place_id++) 
               {
                    sb.append(",");
                    sb.append(String.valueOf(ratings[user][place_id]));
                    
                }
               sb.append("\n");
            }
            
            br.write(sb.toString());
        }
        
                      return ratings;
    }

    private static double[][] map_place_ratings(double[][] normalized_node_value_place_per_user, double[][] ratings_of_user) throws IOException {
        double[][] mapingarray=new double[40000][2];
        int k=0;
        for(int user_id=0;user_id<138;user_id++)
        {
            for(int place_id=0;place_id<130;place_id++)
            {
                if(ratings_of_user[user_id][place_id]==0)
                {
                   
                }
                else
                {
                    mapingarray[k][0]=normalized_node_value_place_per_user[user_id][place_id];
                    mapingarray[k++][1]=ratings_of_user[user_id][place_id];
                }
            }
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter("maping_of_rating_places.csv"))) {
            StringBuilder sb = new StringBuilder();
        int len;
           
            for (int place_id=0;place_id<15000;place_id++) {
                if(mapingarray[place_id][1]!=0)
                {
                sb.append(String.valueOf(mapingarray[place_id][0]));
                
                    sb.append(",");
                    sb.append(String.valueOf(mapingarray[place_id][1]));
                
                
                sb.append("\n");
                }
            }
            
            br.write(sb.toString());
        }
        
        return mapingarray;
    }

    private static double[][] user_cluster(double[][] normalized_distance, int cluster_no) {
        int total_cluster=cluster_no;
        double[] total_distance=new double[total_cluster];
        double[][] cluster=new double[total_cluster][100];
        double []distance=new double[cluster_no];
        double [][]distance_absolute=new double[total_users.total_users][total_users.total_users];
        for(int user_id=0;user_id<total_users.total_users;user_id++)
        {
            for(int p=0;p<total_users.total_users;p++)
            {
                distance_absolute[user_id][p]=Math.abs(172-normalized_distance[user_id][p]);
            }
            
        }
        for(int i=0;i<total_cluster;i++)
        { 
           cluster[i][0]=normalized_distance[i][i];
        }
         for(int k=0;k<50;k++)
         {
             for(int user_id=0;user_id<138;user_id++)
             {
                 
                 for(int cluster_number=0;cluster_number<total_cluster;cluster_number++)
                 {
               
                 }
             }
         }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static double[][] map_with_noncommon(double[][] normalized_node_value_place_per_user, double[][] ratings_of_user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    private static double[][] increase_ratings(double[][] ratings_of_user, double[][] normalized_node_value_place_per_user, double[][] normalized_distance) throws IOException {
        double distance_between_users_places[][]=new double[138][130];
        int user_id=0;
        int place_id=0;
        BufferedReader reader ;
        String line=null;
        Scanner scanner = null;
        int i=0;
        user_id=0;
        int attribute = 0;
        double high_rated=0;
        double s;
        double rating_according_to_user_u=0;
       for( i=0;i<total_users.total_users;i++)
       {
           for(int place=0;place<total_users.total_places;place++)
           {
               if((ratings_of_user[i][place])==1000.00)
               {
                    high_rated=normalized_node_value_place_per_user[i][place];
               
               
                   for(int t=0;t<total_users.total_places;t++)
                   {
                       if(ratings_of_user[i][t]!=0.00)
                       {
                           
                           double distance=(high_rated-normalized_node_value_place_per_user[i][t])*(high_rated-normalized_node_value_place_per_user[i][t]);
                           if(ratings_of_user[i][t]==1500)
                           {
                               ratings_of_user[i][t]=1000;
                           }
                           s=ratings_of_user[i][t]-(distance/20);
                           ratings_of_user[i][t]=(ratings_of_user[i][t]+s)/2;
                           if(ratings_of_user[i][t]<0)
                           {
                               ratings_of_user[i][t]=200;
                           }
                          
                       }
                   }
               
           }
       }
           
       }
            for(int user=0;user<total_users.total_users;user++)
            {
                for(int u=0;u<total_users.total_users;u++)
                {
                    if(Math.abs(normalized_distance[user][u]-normalized_distance[user][user])<5)
                    {         
                        double weight=(Math.abs(normalized_distance[user][u]-normalized_distance[user][user]))/5;
                        
                    for(int place=0;place<total_users.total_places;place++)
                        {
                            if(ratings_of_user[u][place]!=1500 &&ratings_of_user[u][place]!=0)
                            {
                                rating_according_to_user_u=weight*ratings_of_user[u][place];
                                
                            }
                            if(ratings_of_user[user][place]!=1000.00 &&ratings_of_user[user][place]!=0.00)
                            {
                              ratings_of_user[user][place]=(ratings_of_user[user][place]+rating_according_to_user_u)/2;
                            } 
                        }
                    }
                }   
            }
               
                
              try (BufferedWriter br = new BufferedWriter(new FileWriter("increased_rating.csv"))) {
            StringBuilder sb = new StringBuilder();
        int len;
           for(int user=0;user<total_users.total_users;user++)
           {
               sb.append(String.valueOf(ratings_of_user[user][0]));
               for (place_id=0;place_id<total_users.total_places;place_id++) 
               {
                    sb.append(",");
                    sb.append(String.valueOf(ratings_of_user[user][place_id]));
                    
                }
               sb.append("\n");
            }
            
            br.write(sb.toString());
        }
        

        


       return ratings_of_user;
    
}


    private static void get_recommendations(int user_number, double[][] modified_ratings, int[] placeid,String[]place_names) {
        int[] recommended_places=new int[5];
        
        int place=0;
        int t=0;
        for(int i=0;i<5;i++)
        {
            recommended_places[i]=i;
        for( place=0;place<total_users.total_places;place++)
        {
            for(int j=0;j<i;j++)
            {
                if(place==recommended_places[j])
                {
                    
                }
            
                else
                {
        if(modified_ratings[user_number][place]>modified_ratings[user_number][recommended_places[i]])
        {
            if(modified_ratings[user_number][place]!=1000.00)
            {
                recommended_places[i]=place;
            }
            
            //place_names[i]=place_names[place];
        }
        
        }
        }
        int temp=0;
        temp=recommended_places[i];
        recommended_places[i]=placeid[i];
        placeid[i]=temp;
        System.out.println();
        System.out.println(recommended_places[i]);
        }
        
        
    }
    
    }}
        

