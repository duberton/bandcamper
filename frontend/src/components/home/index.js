import { useEffect, useState } from "react";
import { Card, CardTitle, Container, Table } from "reactstrap";
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
          <Card style={{ borderRadius: 10, borderColor: '#00000020' }}>
            <CardTitle style={{ textAlign: 'left', marginLeft: 20, marginTop: 14, fontSize: 20 }}>Albums</CardTitle>
            <Table style={{ fontSize: 15 }}>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Artwork</th>
                  <th>Artist</th>
                  <th>Album</th>
                  <th>Release date</th>
                  <th>Alerts</th>
                  <th>Creation</th>
                </tr>
              </thead>
              <tbody>
                {albums.map((album, index) => {
                  return <tr key={index} >
                    <td style={{ verticalAlign: 'middle' }}>{index + 1}</td>
                    <td style={{ verticalAlign: 'middle'}}><img src={album.albumCoverUrl} alt={album.title} style={{ width: 60, height: 60 }} /></td>
                    <td style={{ verticalAlign: 'middle'}}>{album.artist}</td>
                    <td style={{ verticalAlign: 'middle'}}>{album.title}</td>
                    <td style={{ verticalAlign: 'middle'}}><ReactTimeAgo date={album.releaseDate} /></td>
                    <td style={{ verticalAlign: 'middle'}}><FontAwesomeIcon icon={faEnvelope} /></td>
                    <td style={{ verticalAlign: 'middle'}}><ReactTimeAgo date={album.createdAt} /></td>
                  </tr>
                })}
              </tbody>
            </Table>
          </Card>
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