package boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;

    }

    public abstract boolean[][] possibleMoves();

    /**
     * Verifica se uma posição específica é um movimento possível.
     * @param position A posição de destino que queremos verificar (linha e coluna).
     * @return 'true' se a peça pode se mover para a posição fornecida, 'false' caso contrário.
     */
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /**
     * Verifica se existe PELO MENOS UM movimento possível para a peça.
     * * @return 'true' se houver ao menos um movimento possível, 'false' se a peça
     * estiver sem movimentos.
     */
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Board getBoard() {
        return board;
    }
}
