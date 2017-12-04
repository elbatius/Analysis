/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Kevin-Notebook
 */
public class Level3_Bin {
    private int length;
    private int width;
    private int height;
    
    public int getLength() {
        return this.length;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getVolume() {
        return this.length * this.width * this.height;
    }
    
    @Override
    public String toString() {
        return length + " X " + width + " X " + height;
    }
}
