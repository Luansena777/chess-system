package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private boolean check;
    private boolean checkMate;

    // Cria um tabuleiro 8x8 e configura as peças iniciais
    public ChessMatch() {
        this.board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    /**
     * Retorna uma matriz com as peças atualmente no tabuleiro.
     * Obs: Alterar essa matriz NÃO altera o tabuleiro real.
     */
    public ChessPiece[][] getPieces() {
        ChessPiece[][] matriz = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matriz[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return matriz;
    }

    /**
     * Retorna uma matriz booleana indicando os movimentos possíveis
     * para a peça selecionada.
     *
     * @param sourcePosition Posição da peça no formato de xadrez (ex: "e2")
     * @return matriz booleana com movimentos válidos
     * @throws ChessException se a posição não contém peça válida ou não pertence ao jogador
     */
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    /**
     * Executa um movimento no tabuleiro, validando origem e destino.
     *
     * @param sourcePosition Posição de origem
     * @param targetPosition Posição de destino
     * @return Peça capturada, caso exista
     * @throws ChessException se o movimento não for válido
     */
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode colocar-se em cheque");
        }
        check = (testCheck(opponent(currentPlayer))) ? true : false;
        if (testcheckMate(opponent(currentPlayer))){
            checkMate = true;
        }
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    /**
     * Valida se a posição de origem contém uma peça válida para o jogador atual.
     *
     * @param position posição no tabuleiro
     * @throws ChessException caso a posição não contenha peça,
     *                        não seja do jogador atual
     *                        ou a peça não tenha movimentos possíveis
     */
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não há nenhuma peça na posição de origem");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existe movimentos possiveis para a peça escolhida");
        }
    }

    /**
     * Valida se a posição de destino é um movimento válido para a peça escolhida.
     *
     * @param source posição de origem
     * @param target posição de destino
     * @throws ChessException caso o movimento não seja permitido
     */
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
        }
    }

    /**
     * Executa o movimento da peça no tabuleiro.
     * Caso exista uma peça no destino, ela é capturada.
     *
     * @param source posição de origem
     * @param target posição de destino
     * @return peça capturada, caso exista
     */
    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }

    /**
     * Reverter uma jogada, movendo a peça de volta para sua casa de origem e,
     * se uma peça foi capturada, colocá-la de volta no tabuleiro
     *
     * @param source:        A posição original da peça que foi movida.
     * @param target:        A posição de destino para onde a peça foi movida.
     * @param capturedPiece: A peça que foi capturada durante a jogada. Se nenhuma peça foi capturada, este valor é null.
     */
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        Piece p = board.removePiece(target);
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    /**
     * Determina a cor do oponente.
     * Obtem a cor oposta à cor fornecida.
     *
     * @param color: A cor do jogador atual (WHITE ou BLACK).
     * @return Color.BLACK se a cor de entrada for Color.WHITE, e vice-versa.
     */
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Localizar a peça do tipo King para uma cor específica.
     *
     * @param color: A cor do rei que se deseja encontrar.
     * @return A instância de ChessPiece que corresponde ao rei da cor especificada.
     */
    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == color)
                .toList();

        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não há " + color + " rei no tabuleiro");
    }

    /**
     * Determinar se o rei de um jogador está sob ataque direto de qualquer peça adversária
     *
     * @param color: A cor do jogador que se quer verificar se está em xeque.
     * @return true se o rei estiver em xeque; caso contrário, retorna false
     */
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == opponent(color)).toList();

        for (Piece p : opponentPieces) {
            boolean[][] matriz = p.possibleMoves();
            if (matriz[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testcheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == color).toList();

        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Posiciona uma nova peça no tabuleiro usando coordenadas no formato do xadrez.
     *
     * @param column coluna (a-h)
     * @param row    linha (1-8)
     * @param piece  peça de xadrez
     */
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    // Coloca as peças nas posições iniciais do jogo
    private void initialSetup() {
        placeNewPiece('h', 7, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));

        placeNewPiece('b', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 8, new King(board, Color.BLACK));

    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }
}
