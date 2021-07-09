/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author PC
 */
public class Memoria {
    
    LinkedList<NodoMemoria> L;
    int tamañoMemoria;
    
    public Memoria(int tam) {
        tamañoMemoria=tam;
        L=new LinkedList<>();
        L.add(new NodoMemoria(" ", 0, tamañoMemoria/10));
    }
    
    public int memAvail(){
        int total=tamañoMemoria;
        for(int i=0;i<L.size();i++){    
            total-=L.get(i).getTamaño();
        }return total;
    }
    public int maxAvail(){
        int max=tamañoMemoria-tamañoMemoria/10;
        if(!L.isEmpty()){
            max=L.get(0).getDireccionInicial()-tamañoMemoria/10;
            for(int i=1;i<L.size();i++){    
                int tam=L.get(i).getDireccionInicial()-
                        L.get(i-1).getDireccionFinal()-1;
                if(tam>max){max=tam;}
            }
            int tam=tamañoMemoria-L.getLast().getDireccionFinal()-1;
            if(tam>max){max=tam;}
        }
        return max;
    }
    
    public int getMemFF(String nombre,int tamañoNuevoProceso){
        if(tamañoNuevoProceso>maxAvail()){
            return -1;
        }
        int dirTam=tamañoMemoria/10;
        if(!L.isEmpty()){
            int tam=L.get(0).getDireccionInicial()-tamañoMemoria/10;
            if(tamañoNuevoProceso<=tam){
                NodoMemoria nuevoNodo=new NodoMemoria(nombre,dirTam,tamañoNuevoProceso);
                this.L.add(nuevoNodo);
            }else{
                for(int i=1;i<L.size();i++){    
                tam=L.get(i).getDireccionInicial()-
                            L.get(i-1).getDireccionFinal()-1;
                    if(tamañoNuevoProceso<=tam){
                        dirTam=L.get(i-1).getDireccionFinal()+1;
                        NodoMemoria nuevoNodo=new NodoMemoria
                            (nombre,dirTam,tamañoNuevoProceso);
                        this.L.add(nuevoNodo);
                        ordenar();
                        return dirTam;
                    }
                }
                dirTam=L.getLast().getDireccionFinal()+1;
                NodoMemoria nuevoNodo=new NodoMemoria
                    (nombre,dirTam,tamañoNuevoProceso);
                this.L.add(nuevoNodo);
            }
        }else{
                NodoMemoria nuevoNodo=new NodoMemoria
                    (nombre,dirTam,tamañoNuevoProceso);
                this.L.add(nuevoNodo);
        }
        ordenar();
        return dirTam;
    }
    public int getMemBF(String nombre,int tamañoNuevoProceso){
        if(tamañoNuevoProceso>maxAvail()){
            return -1;
        }
        int dirMejor=tamañoMemoria/10;
        if(!L.isEmpty()){
            int mejor=maxAvail();
            dirMejor=L.getLast().getDireccionFinal()+1;
            int tam=L.getFirst().getDireccionInicial()-tamañoMemoria/10;
            if(tamañoNuevoProceso<=tam && tam<=mejor){
                mejor=tam;
                dirMejor=tamañoMemoria/10;
            }
            for(int i=1;i<L.size();i++){
                tam=L.get(i).getDireccionInicial()-L.get(i-1).getDireccionFinal()-1;
                if(tamañoNuevoProceso<=tam && tam<=mejor){
                    mejor=tam;
                    dirMejor=L.get(i-1).getDireccionFinal()+1;
                }
            }tam=tamañoMemoria-L.getLast().getDireccionFinal()-1;
            if(tamañoNuevoProceso<=tam && tam<mejor){
                dirMejor=L.getLast().getDireccionFinal()+1;
            }
            NodoMemoria nuevoNodo=new NodoMemoria(nombre,dirMejor,tamañoNuevoProceso);
            L.add(nuevoNodo);
        }else{
            NodoMemoria nuevoNodo=new NodoMemoria
                (nombre,dirMejor,tamañoNuevoProceso);
            L.add(nuevoNodo);
        }
        ordenar();
        return dirMejor;
    }
    
    public int[] freeMem(int numProceso){
        String s="P"+Integer.toString(numProceso);
        int dir[]=new int[2];
        for(int i=0;i<L.size();i++){
            if(s.compareTo(L.get(i).getNombre())==0){
                dir[0]=L.get(i).getDireccionInicial();
                dir[1]=L.get(i).getTamaño();
                L.remove(i);
                return dir;
            }
        }return dir;
    }
    
    public String listaProcesoToStr(){
        return listaToStr(L);
    }
    public String listaAreaToStr(){
        int c;int n=0;
        LinkedList<NodoMemoria> aux=new LinkedList<>();
        if(!L.isEmpty()){
            while(n<L.size()){
                int tamaño=L.get(n).getTamaño();
                int dirInicial=L.get(n).getDireccionInicial();
                int dirFinal=L.get(n).getDireccionFinal();
                while(n+1<L.size()&&
                        L.get(n).getDireccionFinal()+1==L.get(n+1).getDireccionInicial()){
                    dirFinal=L.get(n+1).getDireccionFinal();
                    tamaño+=L.get(n+1).getTamaño();
                    n++;
                }n++;
                NodoMemoria nuevoNodo=new NodoMemoria("A1",dirInicial, tamaño);
                aux.add(nuevoNodo);
            }
        }
        return (String)listaToStr(aux);
    }
    private String listaToStr(LinkedList<NodoMemoria> lista){
        String s="<html><body> L:   ";
        int c;
        if(!lista.isEmpty()){
            s=s+lista.get(0).toString();
            c=1;
            for(int i=1;i<lista.size();i++){
                if(c<4){
                    s=s+"->"+lista.get(i).toString();
                    c++;
                }else{//218=┌ 217=┘ 192=└ 191=┐
                    s=s+                                        "┐"+"<br>"
                       +"   ┌------------------------------------┘"+"<br>"
                       +"   └->"+lista.get(i).toString();
                    c=1;
                }
            }
        }s=s+"--||</body></html>";
        return s;
    }
    public String memoriaToStr(){
        String s="";
        s=s+mostrarMonitor();
        for(int dir=tamañoMemoria/10;dir<this.tamañoMemoria;dir++){
            if(esDireccionOcupada(dir)){
                s=s+puestoEnElProceso(dir);
            }else{
                s=s+"       ||";for(int i=1;i<=14;i++){s=s+"  *  ";}s=s+"||"+'\n';
            }
        }
        s=s+"       ";for(int i=1;i<=62;i++){s=s+"-";}s=s+'\n';
        return s;
    }
    
    private String mostrarMonitor(){
        String s="";
        s=s+"000";for(int i=1;i<=62;i++){s=s+"-";}s=s+'\n';
        for(int f=1;f<=tamañoMemoria/10;f++){
            if((tamañoMemoria/10+1)/2!=f){
                s=s+"       ||";for(int i=1;i<=80;i++){s=s+" ";}s=s+"||"+'\n';
            }else{
                s=s+"       ||";
                for(int i=1;i<=24;i++){s=s+" ";}
                s=s+"M  O  N  I  T  O  R ";
                for(int i=1;i<=24;i++){s=s+" ";}
                s=s+"||"+'\n';
            }
        }
        s=s+"       ";for(int i=1;i<=62;i++){s=s+"-";}s=s+'\n';
        return s;
    }
    private boolean esDireccionOcupada(int dir){
        for(int i=0;i<this.L.size();i++){
            if(dir>=this.L.get(i).getDireccionInicial()&&
                    dir<=this.L.get(i).getDireccionFinal()){
                return true;
            }
        }return false;
    }
    private String puestoEnElProceso(int dir){
        String s="";
        int dirFinalProceso=0;
        int dirInicialProceso=0;
        int posDelProcesoEnLista=0;
        for(int i=0;i<this.L.size();i++){
            if(dir>=this.L.get(i).getDireccionInicial()&&
                    dir<=this.L.get(i).getDireccionFinal()){
                dirFinalProceso=L.get(i).getDireccionFinal();
                dirInicialProceso=L.get(i).getDireccionInicial();
                posDelProcesoEnLista=i;
                break;
            }
        }
        int tamañoProceso=dirFinalProceso-dirInicialProceso+1;
        if(dir==dirInicialProceso){
            for(int i=1;i<=68;i++){s=s+"-";}s=s+'\n';
/*TAMAÑO=1*/if(tamañoProceso==1){
                if(dirInicialProceso<10){
/*DirConUnDigito  */s=s+"00"+dirInicialProceso+"||";for(int i=1;i<=38;i++){s=s+" ";}
                }else{
/*DirConDosDigitos*/s=s+"0"+dirInicialProceso+"||";for(int i=1;i<=38;i++){s=s+" ";}
                }
                s=s+L.get(posDelProcesoEnLista).getNombre();
                if(L.get(posDelProcesoEnLista).getNombre().length()==2){
/*ProConUnDigito  */for(int i=1;i<=37;i++){s=s+" ";}s=s+"||"+'\n';
                }else{
/*ProConDosDigitos*/for(int i=1;i<=35;i++){s=s+" ";}s=s+"||"+'\n';
                }
                s=s+"       ";for(int i=1;i<=62;i++){s=s+"-";}s=s+'\n';
/*TAMAÑO=2*/}else if(tamañoProceso==2){
                if(dirInicialProceso<10){    
/*DirConUnDigito  */s=s+"00"+dirInicialProceso+"||";for(int i=1;i<=38;i++){s=s+" ";}
                }else{
/*DirConDosDigitos*/s=s+"0"+dirInicialProceso+"||";for(int i=1;i<=38;i++){s=s+" ";}
                }
                s=s+L.get(posDelProcesoEnLista).getNombre();
                if(L.get(posDelProcesoEnLista).getNombre().length()==2){
/*ProConUnDigito  */for(int i=1;i<=37;i++){s=s+" ";}s=s+"||"+'\n';
                }else{
/*ProConDosDigitos*/for(int i=1;i<=35;i++){s=s+" ";}s=s+"||"+'\n';
                }
            }else{
                if(dirInicialProceso<10){
/*DirConUnDigito  */s=s+"00"+dirInicialProceso+"||";for(int i=1;i<=80;i++){s=s+" ";}s=s+"||"+'\n';
                }else{
/*DirConDosDigitos*/s=s+"0"+dirInicialProceso+"||";for(int i=1;i<=80;i++){s=s+" ";}s=s+"||"+'\n';
                }
            }
        }else if((tamañoProceso+1)/2==dir-dirInicialProceso+1){
            s="       ||";for(int i=1;i<=38;i++){s=s+" ";}
            s=s+L.get(posDelProcesoEnLista).getNombre();
            if(L.get(posDelProcesoEnLista).getNombre().length()==2){
/*ProConUnDigito  */for(int i=1;i<=37;i++){s=s+" ";}s=s+"||"+'\n';
                }else{
/*ProConDosDigitos*/for(int i=1;i<=35;i++){s=s+" ";}s=s+"||"+'\n';
                }
        }else if(dir==dirFinalProceso){
            s="       ||";for(int i=1;i<=80;i++){s=s+" ";}s=s+"||"+'\n';
            s=s+"       ";for(int i=1;i<=62;i++){s=s+"-";}s=s+'\n';
        }else{
            s="       ||";for(int i=1;i<=80;i++){s=s+" ";}s=s+"||"+'\n';
        }
        return s;
    }
    private void ordenar(){
        LinkedList<NodoMemoria> aux=new LinkedList<>();
        while(!L.isEmpty()){
            int menDir=tamañoMemoria-1;
            int pos=0;
            for(int i=0;i<L.size();i++){
                if(L.get(i).getDireccionInicial()<menDir){
                    menDir=L.get(i).getDireccionInicial();
                    pos=i;
                }
            }
            aux.add(L.remove(pos));
        }
        L=aux;
    }
}

