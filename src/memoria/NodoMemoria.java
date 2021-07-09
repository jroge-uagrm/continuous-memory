/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoria;

/**
 *
 * @author PC
 */
class NodoMemoria {

    String nombre;
    int direccion, tamaño;

    public NodoMemoria(String nom, int dir, int tam) {
        this.nombre = nom;
        this.direccion = dir;
        this.tamaño = tam;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDireccionInicial() {
        return direccion;
    }

    public int getDireccionFinal() {
        return direccion + tamaño - 1;
    }

    public int getTamaño() {
        return tamaño;
    }

    @Override
    public String toString() {
        String s="";
        if (direccion < 10) {
            s=s+"[0"+this.direccion+"|";
        }else{
            s=s+"["+this.direccion+"|";
        }
        if (tamaño < 10) {
            s=s+"0"+this.tamaño+"]";
        }else{
            s=s+this.tamaño+"]";
        }
        return s;
    }

}
