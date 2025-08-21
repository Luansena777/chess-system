package boardgame;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    /**
     * Cria um tabuleiro com o número de linhas e colunas especificado.
     *
     * @param rows    número de linhas
     * @param columns número de colunas
     * @throws BoardException se as dimensões forem menores que 1
     */
    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos 1 linha e 1 coluna");
        }
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    //Retorna a peça na posição (linha, coluna) especificada.
    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        return this.pieces[row][column];
    }

    //Retorna a peça na posição especificada.
    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        return this.pieces[position.getRow()][position.getColumn()];
    }

    //Coloca uma peça em uma posição no tabuleiro e atualiza sua posição interna.
    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Já existe uma peça na posição (" + position + ")");
        }
        this.pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    /**
     * Remove e retorna a peça da posição informada.
     *
     * @param position posição no tabuleiro
     * @return a peça removida ou null se não houver peça
     * @throws BoardException se a posição não existir
     */
    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro");
        }
        if (piece(position) == null) {
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;

        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    //Verifica se a posição (linha, coluna) existe no tabuleiro.
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
    }

    //Verifica se a posição existe no tabuleiro.
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    //Retorna true se existir uma peça na posição informada.
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
