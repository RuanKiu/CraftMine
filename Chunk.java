public class Chunk
{
  private boolean[][][] terrain;
  public Chunk()
  {
    terrain = new boolean[10][64][64];
    for (int f = 0; f < terrain.length - 1; f++) {
      for (int r = 0; r < terrain[f].length; r++) {
        for (int c = 0; c < terrain[f][r].length;c++) {
          terrain[f][r][c] = true;
        }
      }
    }
  }
  public boolean[][][] getChunk()
  {
    return terrain;
  }
}
