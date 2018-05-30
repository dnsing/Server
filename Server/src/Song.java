
public class Song {
	public String artista;
	public String cancionNombre;
	public String album;
	public String path;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Song() {
		
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getNombre() {
		return cancionNombre;
	}

	public void setNombre(String cancionNombre) {
		this.cancionNombre = cancionNombre;
	}

}
