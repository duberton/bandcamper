import { useEffect, useState } from "react";
import { Media, Container, Row } from "reactstrap";
import { connect } from "react-redux";
import ky from 'ky';

function Content(props) {

  const [albums, setAlbums] = useState([]);

  async function fetchAlbums() {
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const response = await ky.get(`${apiUrl}/v1/album`, {
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${props.accessToken}`
      }
    }).json();
    setAlbums(response);
  }

  useEffect(() => {
    if (props.isAuthenticated) fetchAlbums()
  }, [props.isAuthenticated]);

  return (
    <main>
      <Container fluid style={{ marginTop: 50 }}>
        <Container>
          {albums.map((album, index) => {
            return <Row key={index} style={{ marginBottom: 20 }}>
              <Media>
                <Media left>
                  <img src={album.albumCoverUrl} style={{ width: 150, height: 150 }} alt={album.title} />
                </Media>
                <Media body>
                  <Media heading className="ml-3">
                    {album.artist} - {album.title}
                  </Media>
                </Media>
              </Media>
            </Row>
          })}
        </Container>
      </Container>
    </main>
  );
}

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.auth.isAuthenticated,
    accessToken: state.auth.accessToken
  };
};

export default connect(mapStateToProps)(Content);