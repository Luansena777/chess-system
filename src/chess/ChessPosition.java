package chess;

import boardgame.Position;

public class ChessPosition {
    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Erro ao instanciar ChessPosition. Os valores válidos vão de a1 a h8.");
        }
        this.column = column;
        this.row = row;
    }

    /**
     * Converte as coordenadas de xadrez (ex: 'a', 1)
     * para as coordenadas da matriz interna do tabuleiro (ex: linha 7, coluna 0)
     */
    protected Position toPosition() {
        return new Position(8 - this.row, this.column - 'a');
    }

    /**
     * Converte as coordenadas da matriz (ex: linha 0, coluna 0)
     * de volta para o formato de xadrez (ex: 'a', 8).
     */
    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return " " + this.column + this.row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

}
