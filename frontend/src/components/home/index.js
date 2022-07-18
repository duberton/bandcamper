import { useEffect, useState } from "react";
import { Card, Row, CardTitle, Col, Container, Table } from "reactstrap";
import { connect } from "react-redux";
import axios from 'axios';
import FollowRelease from "../follow";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faAngleRight, faAngleLeft } from '@fortawesome/free-solid-svg-icons'
import ReactTimeAgo from 'react-time-ago'


function Home(props) {

  const [albums, setAlbums] = useState([]);
  const [cursors, setCursors] = useState({});

  async function fetchAlbums(cursor) {
    const params = { ...cursor, limit: 10 };
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const { data } = await axios.get(`${apiUrl}/v1/album`, {
      params,
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
      <Container fluid style={{ margin: 0, backgroundColor: '#000', display: 'flex', paddingTop: 100, height: '100%', justifyContent: 'center' }}>
        <Container fluid style={{ width: '40%', display: 'flex' }}>
          <Table>
            <Row style={{ justifyContent: 'center' }}>
              <h1 style={{ color: '#fff' }}>Follow your bandcamp <span style={{ color: '#4f746e' }}>releases</span></h1></Row>
            <Row><FollowRelease fetchAlbums={fetchAlbums} /></Row>
          </Table>
        </Container>
        <Container fluid style={{ width: '60%', display: 'flex', justifyContent: 'center' }}>
          <Card style={{ width: '80%', height: 'fit-content', borderRadius: 10, borderColor: '#00000020' }}>
            <CardTitle style={{ textAlign: 'left', marginLeft: 20, marginTop: 14, fontSize: 20 }}>Releases</CardTitle>
            <Table style={{ fontSize: 15 }}>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Artwork</th>
                  <th>Artist</th>
                  <th>Album</th>
                  <th>Release date</th>
                  <th>Alerts</th>
                  {/* <th>Creation</th> */}
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
                    {/* <td style={{ verticalAlign: 'middle' }}><ReactTimeAgo date={attributes.createdAt} /></td> */}
                  </tr>
                })}
              </tbody>
            </Table>
            <Container>
              <Row style={{ marginBottom: 20 }}>
                <Col sm="6" style={{ cursor: 'pointer' }} onClick={() => {
                  if (cursors.previous) fetchAlbums({ previous: cursors.previous })
                }}
                ><FontAwesomeIcon icon={faAngleLeft} /> Previous</Col>
                <Col sm="6" style={{ cursor: 'pointer' }} onClick={() => {
                  if (cursors.next) fetchAlbums({ next: cursors.next })
                }}>Next <FontAwesomeIcon icon={faAngleRight} /></Col>
              </Row>
            </Container>
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

export default connect(mapStateToProps)(Home);