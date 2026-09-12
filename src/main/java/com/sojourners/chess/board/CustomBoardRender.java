package com.sojourners.chess.board;

import com.sojourners.chess.util.PathUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomBoardRender extends BaseBoardRender {

    private Image bgImage;
    private Image maskImage;
    private Image mask2Image;
    private Map<Character, Image> map;

    public CustomBoardRender(Canvas canvas) {
        super(canvas);

        this.bgImage = new Image(new File(PathUtils.getJarPath() + "/ui/board.png").toURI().toString());

        this.maskImage = new Image(new File(PathUtils.getJarPath() + "/ui/mask.png").toURI().toString());
        this.mask2Image = new Image(new File(PathUtils.getJarPath() + "/ui/mask2.png").toURI().toString());

        map = new HashMap<>();
        map.put('r', new Image(new File(PathUtils.getJarPath() + "/ui/br.png").toURI().toString()));
        map.put('n', new Image(new File(PathUtils.getJarPath() + "/ui/bn.png").toURI().toString()));
        map.put('b', new Image(new File(PathUtils.getJarPath() + "/ui/bb.png").toURI().toString()));
        map.put('a', new Image(new File(PathUtils.getJarPath() + "/ui/ba.png").toURI().toString()));
        map.put('k', new Image(new File(PathUtils.getJarPath() + "/ui/bk.png").toURI().toString()));
        map.put('c', new Image(new File(PathUtils.getJarPath() + "/ui/bc.png").toURI().toString()));
        map.put('p', new Image(new File(PathUtils.getJarPath() + "/ui/bp.png").toURI().toString()));

        map.put('R', new Image(new File(PathUtils.getJarPath() + "/ui/rr.png").toURI().toString()));
        map.put('N', new Image(new File(PathUtils.getJarPath() + "/ui/rn.png").toURI().toString()));
        map.put('B', new Image(new File(PathUtils.getJarPath() + "/ui/rb.png").toURI().toString()));
        map.put('A', new Image(new File(PathUtils.getJarPath() + "/ui/ra.png").toURI().toString()));
        map.put('K', new Image(new File(PathUtils.getJarPath() + "/ui/rk.png").toURI().toString()));
        map.put('C', new Image(new File(PathUtils.getJarPath() + "/ui/rc.png").toURI().toString()));
        map.put('P', new Image(new File(PathUtils.getJarPath() + "/ui/rp.png").toURI().toString()));
    }

    @Override
    public void drawBackgroundImage(double width, double height) {
        gc.drawImage(bgImage, 0, 0, width, height);
    }

    @Override
    public void drawCenterText(int pos, int piece, ChessBoard.BoardSize style) {

    }

    @Override
    public void drawBoardLine(int pos, int padding, int piece, boolean isReverse, ChessBoard.BoardSize style) {

    }


    @Override
    public void drawPieces(int pos, int piece, char[][] board, boolean isReverse, ChessBoard.BoardSize style, boolean pieceShadow, double pieceScale) {
        // 棋子半径跟着 scale 缩放
        int r = (int) Math.round((piece - piece / 16) / 2 * pieceScale);

        // 图片自带阴影时，视觉中心不在图片几何中心，需要偏移
        double offX = r * this.getPieceOffsetX();
        double offY = r * this.getPieceOffsetY();

        if (!pieceShadow) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    Image img = map.get(board[i][j]);
                    if (img != null) {
                        int x = pos + piece * getReverseX(j, isReverse);
                        int y = pos + piece * getReverseY(i, isReverse);
                        gc.drawImage(img, x - r + offX, y - r + offY, 2 * r, 2 * r);
                    }
                }
            }
            return;
        }

        DropShadow ambient = new DropShadow();
        ambient.setRadius(r * 0.18);
        ambient.setOffsetX(r * 0.22);
        ambient.setOffsetY(r * 0.24);
        ambient.setSpread(0.0);
        ambient.setColor(Color.rgb(45, 30, 15, 0.45));   // 不动

        DropShadow contact = new DropShadow();
        contact.setRadius(r * 0.10);
        contact.setOffsetX(r * 0.05);
        contact.setOffsetY(r * 0.06);
        contact.setSpread(0.35);
        contact.setColor(Color.rgb(35, 20, 10, 0.65));
        contact.setInput(ambient);

        gc.save();
        gc.setEffect(contact);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Image img = map.get(board[i][j]);
                if (img == null) continue;
                int x = pos + piece * getReverseX(j, isReverse);
                int y = pos + piece * getReverseY(i, isReverse);
                gc.drawImage(img, x - r + offX, y - r + offY, 2 * r, 2 * r);
            }
        }

        gc.restore();
    }

    @Override
    public Color getBackgroundColor() {
        int centerX = (int) (bgImage.getWidth() / 2);
        int centerY = (int) (bgImage.getHeight() / 2);
        return this.bgImage.getPixelReader().getColor(centerX, centerY);
    }

    @Override
    public void drawStepRemark(int pos, int piece, int x, int y, boolean isPrevStep, boolean isReverse, ChessBoard.BoardSize style) {

        int r = (piece - piece / 32) / 2;

        x = pos + piece * getReverseX(x, isReverse);
        y = pos + piece * getReverseY(y, isReverse);

        Image img = isPrevStep ? mask2Image : maskImage;
        gc.drawImage(img, x - r, y - r, 2 * r, 2 * r);
    }
}
