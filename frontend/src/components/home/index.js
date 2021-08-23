import { useEffect, useState } from "react";
import { Media, Container, Table } from "reactstrap";
import { connect } from "react-redux";
import ky from 'ky';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope } from '@fortawesome/free-solid-svg-icons'
import ReactTimeAgo from 'react-time-ago'


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
      <Container fluid style={{ marginTop: 100 }}>
        <Container>
          <Table>
            <thead>
              <tr>
                <th>#</th>
                <th>Artist</th>
                <th>Album</th>
                <th>Release date</th>
                <th>Alerts</th>
                <th>Creation</th>
              </tr>
            </thead>
            <tbody>
              {albums.map((album, index) => {
                return <tr key={index}>
                  <td><img src={album.albumCoverUrl} alt={album.title} style={{ width: 60, height: 60 }} /></td>
                  <td>{album.artist}</td>
                  <td>{album.title}</td>
                  <td><ReactTimeAgo date={album.releaseDate} /></td>
                  {/* <td>{album.isReleased == false ? 'yes' : 'no'}</td> */}
                  <td><FontAwesomeIcon icon={faEnvelope} /></td>
                  <td><ReactTimeAgo date={album.createdAt} /></td>
                </tr>
              })}
            </tbody>
          </Table>
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