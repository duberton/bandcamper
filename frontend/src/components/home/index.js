import { useEffect, useState } from "react";
import { Card, CardTitle, Container, Table } from "reactstrap";
import { connect } from "react-redux";
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope } from '@fortawesome/free-solid-svg-icons'
import ReactTimeAgo from 'react-time-ago'


function Content(props) {

  const [albums, setAlbums] = useState([]);
  const [cursors, setCursors] = useState({});

  async function fetchAlbums() {
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const { data } = await axios.get(`${apiUrl}/v1/album`, {
      params: { previous: cursors.previous, next: cursors.next },
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${props.accessToken}`
      }
    });
    setAlbums(data.data);
    setCursors(data.cursors);
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
                {albums.map((resource, index) => {
                  const attributes = resource.attributes;
                  return <tr key={index} >
                    <td style={{ verticalAlign: 'middle' }}>{index + 1}</td>
                    <td style={{ verticalAlign: 'middle' }}><img src={attributes.albumCoverUrl} alt={attributes.title} style={{ width: 60, height: 60 }} /></td>
                    <td style={{ verticalAlign: 'middle' }}>{attributes.artist}</td>
                    <td style={{ verticalAlign: 'middle' }}>{attributes.title}</td>
                    <td style={{ verticalAlign: 'middle' }}><ReactTimeAgo date={attributes.releaseDate} /></td>
                    <td style={{ verticalAlign: 'middle' }}><FontAwesomeIcon icon={faEnvelope} /></td>
                    <td style={{ verticalAlign: 'middle' }}><ReactTimeAgo date={attributes.createdAt} /></td>
                  </tr>
                })}
              </tbody>
              <tfoot>
                <tr>
                  <td>Previous</td>
                  <td onClick={fetchAlbums}>Next</td>
                </tr>
              </tfoot>
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