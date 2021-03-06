public class GameManager
{
    private Player[][] board;
    private Player player1;
    private Player player2;
    
    public GameManager()
    {
        this.board = new Player[6][7];
        this.player1 = new Player(1, " ");
        this.player2 = new Player(2, " ");
    }
    
    public void setPlayer1(Player p)
    {
        this.player1 = p;
    }
    
    public void setPlayer2(Player p)
    {
        this.player2 = p;
    }
    
    public Player[][] getBoard()
    {
        return this.board;
    }
    
    public boolean isEmpty()
    {
        for (int i = 0; i < this.board.length; i++)
            for (int y = 0; y < this.board[0].length; y++)
                if (this.board[i][y] != null)
                    return false;
        return true;
    }
    
    public boolean isFull()
    {
        for (int i = 0; i < this.board.length; i++)
            for (int y = 0; y < this.board[0].length; y++)
                if (this.board[i][y] == null)
                    return false;
        return true;
    }
    
    public void placePiece(Location loc, Player person) {
        if ((person instanceof AIPlayer))
        {
            BestCol tester = analyzeBoard(Boolean.valueOf(false), 0);
            while (getLocAbovePieceInCol(tester.getColumn()) == null)
            {
                tester.setColumn((int)Math.random() * 7);
            }
            Location x = getLocAbovePieceInCol(tester.getColumn());
            this.board[x.getRow()][x.getCol()] = person;
        }
        else {
            this.board[loc.getRow()][loc.getCol()] = person;
        }
    }
    
    public void removePiece(Location loc) {
        this.board[loc.getRow()][loc.getCol()] = null;
    }
    
    public boolean checkCol(int col)
    {
        for (int i = 0; i < 6; i++)
            if (this.board[i][col] == null)
                return true;
        return false;
    }
    
    public Location getLocAbovePieceInCol(int col)
    {
        for (int i = 0; i < 6; i++)
            if (this.board[i][col] == null)
                return new Location(i, col);
        return null;
    }
    
    public BestCol analyzeBoard(Boolean person, int recursionLimit)
    {
        BestCol bestCol = new BestCol(person.booleanValue());
        
        if ((!isFull()) && (!hasWon(person.booleanValue()))) { if ((!hasWon(!person.booleanValue())) && (recursionLimit != 6));
        } else {
            bestCol.setScore(evaluateScore());
            return bestCol;
        }
        
        if (!person.booleanValue())
        {
            bestCol.setScore(-2147483648);
        }
        else
        {
            bestCol.setScore(2147483647);
        }
        
        recursionLimit++;
        for (int i = 0; i < this.board[0].length; i++)
        {
            if (checkCol(i))
            {
                Location loc = getLocAbovePieceInCol(i);
                boolean replyBool;
                boolean replyBool;
                if (!person.booleanValue())
                {
                    this.board[loc.getRow()][i] = this.player2;
                    replyBool = true;
                }
                else
                {
                    this.board[loc.getRow()][i] = this.player1;
                    replyBool = false;
                }
                
                BestCol reply = analyzeBoard(Boolean.valueOf(replyBool), recursionLimit);
                
                this.board[loc.getRow()][i] = null;
                
                if ((!person.booleanValue()) && (reply.getScore() > bestCol.getScore()))
                {
                    bestCol.setColumn(i);
                    bestCol.setScore(reply.getScore());
                }
                else if ((person.booleanValue()) && (reply.getScore() < bestCol.getScore()))
                {
                    bestCol.setColumn(i);
                    bestCol.setScore(reply.getScore());
                }
            }
        }
        return bestCol;
    }
    
    public boolean hasWon(boolean b)
    {
        Player p;
        Player p;
        if (!b)
            p = this.player2;
        else
            p = this.player1;
        for (int i = 0; i < this.board.length; i++)
            for (int j = 0; j < this.board[0].length; j++)
            {
                Player o = this.board[i][j];
                if ((o != null) && (p.getID() == o.getID()))
                {
                    int diagonal = 0;
                    int diagonalBack = 0;
                    int vertical = 0;
                    int horizontal = 0;
                    for (int t = 0; t < 4; t++)
                    {
                        if (i <= 2)
                        {
                            Player testerVertical = this.board[(i + t)][j];
                            if ((testerVertical != null) && (testerVertical.getID() == o.getID())) {
                                vertical++;
                            }
                        }
                        if (j <= 3)
                        {
                            Player testerHorizontal = this.board[i][(j + t)];
                            if ((testerHorizontal != null) && (testerHorizontal.getID() == o.getID())) {
                                horizontal++;
                            }
                        }
                        if ((i <= 2) && (j <= 3))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j + t)];
                            if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID())) {
                                diagonal++;
                            }
                        }
                        if ((i <= 2) && (j >= 3))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j - t)];
                            if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()))
                                diagonalBack++;
                        }
                    }
                    if ((vertical == 4) || (horizontal == 4) || (diagonal == 4) || (diagonalBack == 4))
                        return true;
                }
            }
        return false;
    }
    
    public int evaluateScore()
    {
        if (hasWon(false))
            return 2147483647;
        if (hasWon(true))
            return -2147483648;
        if (isFull())
            return 0;
        return checkThrees(false) * 10000 + checkTwos(false) * 100 - (checkThrees(true) * 10000 + checkTwos(true) * 100);
    }
    
    public int checkThrees(boolean b)
    {
        Player p;
        Player p;
        if (!b)
            p = this.player2;
        else
            p = this.player1;
        int numThrees = 0;
        for (int i = 0; i < this.board.length; i++)
            for (int j = 0; j < this.board[0].length; j++)
            {
                Player o = this.board[i][j];
                if ((o != null) && (p.getID() == o.getID()))
                {
                    int diagonal = 0;
                    int diagonalBack = 0;
                    int vertical = 0;
                    int horizontal = 0;
                    for (int t = 0; t < 3; t++)
                    {
                        if (i < 3)
                        {
                            Player testerVertical = this.board[(i + t)][j];
                            if ((testerVertical != null) && (testerVertical.getID() == o.getID()) && (this.board[(i + 3)][j] == null)) {
                                vertical++;
                            }
                        }
                        if (j < 4)
                        {
                            Player testerHorizontal = this.board[i][(j + t)];
                            if (i < 1)
                            {
                                if ((testerHorizontal != null) && (testerHorizontal.getID() == o.getID()) && ((this.board[i][(j + 3)] == null) || ((j > 0) && (this.board[i][(j - 1)] == null)))) {
                                    horizontal++;
                                }
                                
                            }
                            else if ((testerHorizontal != null) && (testerHorizontal.getID() == o.getID()) && (this.board[(i - 1)][(j + t)] != null) && ((this.board[i][(j + 3)] == null) || ((j > 0) && (this.board[i][(j - 1)] == null)))) {
                                horizontal++;
                            }
                        }
                        
                        if ((i < 3) && (j < 4))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j + t)];
                            if (i < 1)
                            {
                                if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i + 3)][(j + 3)] == null)) {
                                    diagonal++;
                                }
                                
                            }
                            else if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i - 1 + t)][(j + t)] != null) && ((this.board[(i + 3)][(j + 3)] == null) || ((j > 0) && (this.board[(i - 1)][(j - 1)] == null)))) {
                                diagonal++;
                            }
                        }
                        
                        if ((i < 3) && (j >= 3))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j - t)];
                            if (i < 1)
                            {
                                if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i + 3)][(j - 3)] == null)) {
                                    diagonalBack++;
                                }
                                
                            }
                            else if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i - 1 + t)][(j - t)] != null) && ((this.board[(i + 3)][(j - 3)] == null) || ((j < 6) && (this.board[(i - 1)][(j + 1)] == null)))) {
                                diagonalBack++;
                            }
                        }
                    }
                    
                    if (((horizontal == 3) && (j > 0) && (this.board[i][(j - 1)] == null) && (j < 4) && (this.board[i][(j + 3)] == null)) || ((diagonal == 3) && (i > 0) && (j > 0) && (this.board[(i - 1)][(j - 1)] == null) && (i < 3) && (j < 4) && (this.board[(i + 3)][(j + 3)] == null)) || ((diagonalBack == 3) && (i > 0) && (j < 6) && (this.board[(i - 1)][(j + 1)] == null) && (i < 3) && (j >= 3) && (this.board[(i + 3)][(j - 3)] == null)))
                    {
                        numThrees += 10;
                    }
                    else if ((vertical == 3) || (horizontal == 3) || (diagonal == 3) || (diagonalBack == 3))
                        numThrees++;
                }
            }
        return numThrees;
    }
    
    public int checkTwos(boolean b)
    {
        Player p;
        Player p;
        if (!b)
            p = this.player2;
        else
            p = this.player1;
        int numTwos = 0;
        for (int i = 0; i < this.board.length; i++)
            for (int j = 0; j < this.board[0].length; j++)
            {
                Player o = this.board[i][j];
                if ((o != null) && (p.getID() == o.getID()))
                {
                    int diagonal = 0;
                    int diagonalBack = 0;
                    int vertical = 0;
                    int horizontal = 0;
                    for (int t = 0; t < 2; t++)
                    {
                        if (i < 3)
                        {
                            Player testerVertical = this.board[(i + t)][j];
                            if ((testerVertical != null) && (testerVertical.getID() == o.getID()) && (this.board[(i + 2)][j] == null) && (this.board[(i + 3)][j] == null)) {
                                vertical++;
                            }
                        }
                        if (j < 4)
                        {
                            Player testerHorizontal = this.board[i][(j + t)];
                            if (i < 1)
                            {
                                if ((testerHorizontal != null) && (testerHorizontal.getID() == o.getID()) && (((this.board[i][(j + 2)] == null) && (this.board[i][(j + 3)] == null)) || ((j > 1) && (this.board[i][(j - 1)] == null) && (this.board[i][(j - 2)] == null)))) {
                                    horizontal++;
                                }
                                
                            }
                            else if ((testerHorizontal != null) && (testerHorizontal.getID() == o.getID()) && (this.board[(i - 1)][(j + t)] != null) && (((this.board[i][(j + 2)] == null) && (this.board[i][(j + 3)] == null)) || ((i > 1) && (j > 1) && (this.board[i][(j - 1)] == null) && (this.board[i][(j - 2)] == null)))) {
                                horizontal++;
                            }
                        }
                        
                        if ((i < 3) && (j < 4))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j + t)];
                            if (i < 1)
                            {
                                if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i + 2)][(j + 2)] == null) && (this.board[(i + 3)][(j + 3)] == null)) {
                                    diagonal++;
                                }
                                
                            }
                            else if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i - 1 + t)][(j + t)] != null) && (((this.board[(i + 2)][(j + 2)] == null) && (this.board[(i + 3)][(j + 3)] == null)) || ((i > 1) && (j > 1) && (this.board[(i - 1)][(j - 1)] == null) && (this.board[(i - 2)][(j - 2)] == null)))) {
                                diagonal++;
                            }
                        }
                        
                        if ((i < 3) && (j >= 3))
                        {
                            Player testerDiagonal = this.board[(i + t)][(j - t)];
                            if (i < 1)
                            {
                                if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i + 2)][(j - 2)] == null) && (this.board[(i + 3)][(j - 3)] == null)) {
                                    diagonalBack++;
                                }
                                
                            }
                            else if ((testerDiagonal != null) && (testerDiagonal.getID() == o.getID()) && (this.board[(i - 1 + t)][(j - t)] != null) && (((this.board[(i + 2)][(j - 2)] == null) && (this.board[(i + 3)][(j - 3)] == null)) || ((i > 1) && (j < 5) && (this.board[(i - 1)][(j + 1)] == null) && (this.board[(i - 2)][(i + 2)] == null)))) {
                                diagonalBack++;
                            }
                        }
                    }
                    if (((horizontal == 2) && (j > 1) && (this.board[i][(j - 1)] == null) && (this.board[i][(j - 2)] == null) && (j < 4) && (this.board[i][(j + 2)] == null) && (this.board[i][(j + 3)] == null)) || ((diagonal == 2) && (i > 1) && (j > 1) && (this.board[(i - 1)][(j - 1)] == null) && (this.board[(i - 2)][(j - 2)] == null) && (i < 3) && (j < 4) && (this.board[(i + 2)][(j + 2)] == null) && (this.board[(i + 3)][(j + 3)] == null)) || ((diagonalBack == 2) && (i > 1) && (j < 5) && (this.board[(i - 1)][(j + 1)] == null) && (this.board[(i - 2)][(i + 2)] == null) && (i < 3) && (j >= 3) && (this.board[(i + 2)][(j - 2)] == null) && (this.board[(i + 3)][(j - 3)] == null)))
                    {
                        numTwos += 10;
                    }
                    else if ((vertical == 3) || (horizontal == 3) || (diagonal == 3) || (diagonalBack == 3))
                        numTwos++;
                }
            }
        return numTwos;
    }
    
    public void reset()
    {
        this.board = new Player[6][7];
    }
}