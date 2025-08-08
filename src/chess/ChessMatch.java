package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        this.board = new Board(8, 8);
        initialSetup();
    }

    //Retorna a matriz de peças do xadrez
    public ChessPiece[][] getPieces() {
        ChessPiece[][] matriz = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matriz[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return matriz;
    }

    //Iniciar o tabuleiro com as peças no lugar inicial
    private void initialSetup(){
        board.PlacePiece(new Rook(board, Color.WHITE), new Position(2,1));
        board.PlacePiece(new King(board, Color.BLACK), new Position(3,1));

        board.PlacePiece(new King(board, Color.WHITE), new Position(7,4));
        board.PlacePiece(new Rook(board, Color.WHITE), new Position(7,1));

    }
}
