public class Trie
{
    private int SIGMA, MAXN, size;
    private int[][] ch;
    private User[] info;

    public Trie(int MAXN, int sigma)
    {
        this.SIGMA = sigma;
        this.MAXN = MAXN;
        this.ch = new int[this.MAXN][];
        this.ch[this.size = 0] = new int[this.SIGMA];
        this.info = new User[this.MAXN];
    }

    public Trie(int MAXN)
    {
        this(MAXN, 64);
    }

    static private int idx(char ch)
    {
        if(ch >= '0' && ch <= '9') //0-9
            return ch - '0';
        if(ch >= 'a' && ch <= 'z') //10 - 35;
            return ch - 'a' + 10;
        if(ch >= 'A' && ch <= 'Z') //36 - 61;
            return ch - 'A' + 36;
        if(ch == '_') //62
            return 62;
        if(ch == ' ') //63
            return 63;
        return -1;
    }

    /**
     * insert a string into trie with attatched info
     * @param s
     * @param info
     */
    public void insert(String s, User info)
    {
        int len = s.length(), u = 0, c;
        for(int i = 0; i < len; ++i)
        {
            c = idx(s.charAt(i));
            if(ch[u][c] == 0)
            {
                ++this.size;
                this.ch[size] = new int[SIGMA];
                this.ch[u][c] = size;
            }
            u = this.ch[u][c];
        }
        this.info[u] = info;
    }

    /**
     * find a certain string
     * @param s
     * @return the attached info, null if not found
     */
    public User find(String s)
    {
        int len = s.length(), u = 0, c;
        for(int i = 0; i < len; ++i)
        {
            c = idx(s.charAt(i));
            if(this.ch[u][c] == 0)
                return null;
            u = this.ch[u][c];
        }
        return info[u];
    }
}
