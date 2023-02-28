public class Trie
{
    private int SIGMA, MAXN, size;
    private int[][] ch;
    private int[] info;

    public Trie(int MAXN, int sigma)
    {
        this.SIGMA = sigma;
        this.MAXN = MAXN;
        this.ch = new int[MAXN][];
        this.ch[this.size = 0] = new int[SIGMA];
        this.info = new int[MAXN];
    }

    public Trie(int MAXN)
    {
        this(MAXN, 64);
    }

    private int idx(char ch)
    {
        /* get number index for ch */
        if(ch >= '0' && ch <= '9') //0-9
            return ch - '0';
        if(ch >= 'a' && ch <= 'z') //10 - 35;
            return ch = 'a' + 10;
        if(ch >= 'A' && ch <= 'Z') //36 - 61;
            return ch - 'A' + 36;
        if(ch == '_') //62
            return 62;
        if(ch == ' ') //63
            return 63;
        return -1;
    }

    public void clear()
    {
        this.ch = new int[MAXN][];
        this.ch[this.size = 0] = new int[SIGMA];
        this.info = new int[MAXN];
    }

    public void insert(String s, int info)
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

    public int find(String s)
    {
        // return the node index of S
        int len = s.length(), u = 0, c;
        for(int i = 0; i < len; ++i)
        {
            c = idx(s.charAt(i));
            if(this.ch[u][c] == 0)
                return 0;
            u = this.ch[u][c];
        }
        return info[u];
    }
}
