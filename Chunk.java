public class Chunk
{
  private boolean[][][] terrain;
  public Chunk()
  {
    terrain = new boolean[2][30][30];
    for (int f = 0; f < terrain.length - 1; f++) {
      for (int r = 0; r < terrain[f].length; r++) {
        for (int c = 0; c < terrain[f][r].length;c++) {
          if (Math.random() >= 0.5)
            terrain[f][r][c] = true;
          else
            terrain[f][r][c] = false;
        }
      }
    }
  }
  public boolean[][][] getChunk()
  {
    return terrain;
  }
}
