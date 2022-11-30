
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageManipulation {
        private BufferedImage img;
        private boolean[][] grid;
        private int height;
        private int width;


        public ImageManipulation(String pathName, double scale) throws IOException {
                File file = new File(pathName);
                this.img = ImageIO.read(file);
                this.width = img.getWidth() + 1;
                this.height = img.getHeight() + 1;

                this.img = resize(this.img, scale);
                this.grid = new boolean[height][width];


                this.buildBarriers();
        }

        public void buildBarriers(){
                for (int y = 0; y < this.height; y++) {
                        for (int x = 0; x < this.width; x++) {

                                int p = this.img.getRGB(x, y);
                                int red = (p & 0x00ff0000) >> 16;
                                int green = (p & 0x0000ff00) >> 8;
                                int blue = p & 0x000000ff;

                                if (red != 255 && green != 255 && blue != 255) {
                                        this.grid[y][x] = true;
                                }else{
                                        this.grid[y][x] = false;
                                }

                        }
                }
        }



        private BufferedImage resize(BufferedImage inputImage, double scale) throws IOException {
                /**
                 * Using scale:
                 * ...
                 * ...
                 * 30% -> 0.30 (oldWidth/3, oldHeight/3)
                 * 50% -> 0.50 (oldWidth/2, oldHeight/2)
                 * 90% -> 0.90
                 * 100% -> 1.00 (same Width and Height)
                 * 200% -> 2.00 (oldWidth * 2 and oldHeight * 2)
                 * ...
                 * ...
                 **/
                if(scale > 0){
                        double newWidth = this.width * scale;
                        double newHeight = this.height * scale;

                        int scaledWidth = (int) Math.round(newWidth);
                        int scaledHeight = (int) Math.round(newHeight);

                        BufferedImage outputImage = new BufferedImage(
                                scaledWidth,
                                scaledHeight,
                                inputImage.getType()
                        );


                        Graphics2D g2d = outputImage.createGraphics();
                        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
                        g2d.dispose();

                        return outputImage;
                }else{
                        throw new IOException("Error: ImageManipulation.resize --> |required scale > 0 \n any scale value <= 0 not accepted|");
                }
        }

        public void drawPath(ArrayList<ArrayList<Node>> path, String writtenImagePath, Color color, int dimPen) throws IOException {
                //Recommanded dimPen value: 1

                if(color == null){
                        color = new Color(0,0,0);
                        System.out.println("[ImageManipulation] drawPath WARNING: inserted color value is null\nDefault Settings: new Color(0, 0, 0) (Black)\n");
                }

                if(dimPen < 0 || dimPen > 4) {
                        dimPen = 1;
                        System.out.println("[ImageManipulation] drawPath WARNING: inserted dimPen value not accepted\nDefault Settings: dimPen = 1\n");
                }
                int rgb = color.getRGB();
                if (this.img != null) {
                        if (path != null) {
                                File f = new File(writtenImagePath);
                                for (ArrayList<Node> slicedPath : path) {
                                        for (Node point : slicedPath) {
                                                if(dimPen == 1){
                                                        img.setRGB(point.y, point.x, rgb);
                                                }
                                                if(dimPen == 2){
                                                        img.setRGB(point.y, point.x, rgb);

                                                        img.setRGB(point.y, point.x - 1, rgb);
                                                        img.setRGB(point.y, point.x + 1, rgb);
                                                        img.setRGB(point.y - 1, point.x, rgb);
                                                        img.setRGB(point.y + 1, point.x, rgb);
                                                }
                                                if(dimPen == 3){
                                                        img.setRGB(point.y, point.x, rgb);

                                                        img.setRGB(point.y, point.x - 1, rgb);
                                                        img.setRGB(point.y, point.x + 1, rgb);
                                                        img.setRGB(point.y - 1, point.x, rgb);
                                                        img.setRGB(point.y + 1, point.x, rgb);

                                                        img.setRGB(point.y - 1, point.x + 1, rgb);
                                                        img.setRGB(point.y + 1, point.x + 1, rgb);
                                                        img.setRGB(point.y - 1, point.x - 1, rgb);
                                                        img.setRGB(point.y + 1, point.x - 1, rgb);
                                                }
                                                ImageIO.write(img, "jpg", f);
                                        }

                                }
                        } else throw new Error("[ImageManipulation] drawPath: Path is null");
                } else throw new Error("[ImageManipulation] drawPath: BufferedImage is null");


        }

        /*int colorToUse = new Color(0,0,255).getRGB();

        if (path != null) {
                File f = new File("/home/savc18/Documents/Repo/AStar_PathFinding/src/main/java/path.jpg");
                for(ArrayList<Node> slicedPath : path){
                        for(Node point: slicedPath){
                                img.setRGB(point.y, point.x, colorToUse);
                                ImageIO.write(img, "jpg", f);
                        }

                }
        }*/

        public BufferedImage getImg() { return img; }

        public void setImg(BufferedImage img) {
                this.img = img;
        }

        public boolean[][] getGrid() {
                return grid;
        }

        public void setGrid(boolean[][] grid) {
                this.grid = grid;
        }

        public int getHeight() {
                return height;
        }

        public void setHeight(int height) {
                this.height = height;
        }

        public int getWidth() {
                return width;
        }

        public void setWidth(int width) {
                this.width = width;
        }
}