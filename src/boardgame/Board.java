package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos 1 linha e 1 coluna");
        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    //Colocar uma determinada peça em uma determinada posicao
    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        return this.pieces[row][column];
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        return this.pieces[position.getRow()][position.getColumn()];
    }

    //Colocar peça em uma dada posição no tabuleiro
    public void PlacePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Já existe uma peça posicionada " + position);
        }
        this.pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    //Verificar se uma peça existe
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    //Verifica se tem uma peça na posiçao passada
    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        return piece(position) != null;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

}
