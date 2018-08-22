package com.example.new_application;

public class loading_algorithm {
    private int mcurrentload;
    private int mmaxload;

    public loading_algorithm(int currentload, int maxload){
            mcurrentload = currentload;
            mmaxload = maxload;

    }
    public void setcurrentomax(){
        this.mcurrentload = this.mmaxload;

    }

    public int getIncrement(){
        int temp;
        temp = mcurrentload/2;
        if(temp <= 10  && temp > 3){
            updatecurrentload(temp);
            return temp;
        }else if(temp > 10){
            temp = 10;
            updatecurrentload(temp);
            return temp;
        }else if(temp <= 3){
          //  temp = this.mcurrentload;
            updatecurrentload(temp);
            return temp;
        }else if(temp == 0){
            temp = 0;
            return temp;
        }else{
            temp = 0;
            return temp;
        }
    }

    private void updatecurrentload(int temp) {
        this.mcurrentload = this.mcurrentload - temp;

    }
    public int getCurrentload(){
        return this.mcurrentload;
    }

}


