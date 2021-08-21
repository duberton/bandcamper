import { useEffect, useState } from "react";
import { Col, Container, Row, Navbar, NavbarBrand, Card, CardTitle, CardText, CardSubtitle, CardBody, CardImg } from "reactstrap";
import ky from 'ky';

function Content() {

  const [albums, setAlbums] = useState([]);

  async function fetchAlbums() {
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const response = await ky.get(`${apiUrl}/v1/album`, {
      headers: {
        'Accept': 'application/json',
        'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqd3QtYXV0aCIsImlzcyI6ImNvbS5kdWJlcnRvbi5iYW5kY2FtcGVyIiwibmFtZSI6IkVkdWFyZG8gQmVydG9uIiwiZXhwIjoxNjI5NTczMDE1LCJlbWFpbCI6ImR1YmVydG9uQGdtYWlsLmNvbSJ9.HQMjTvklANPw9Zm5t4QsepZg7ePaOk7twemQpmzTgqOTla1jRX-ljyNmyRZNCVkvk-SKIozUL9jqzjTvAsEbdg'
      }
    }).json();
    setAlbums(response);
  }

  useEffect(() => {
    fetchAlbums()
  }, []);

  return (
    <main>
      <Container fluid style={{ marginTop: 50 }}>
        <Container>
          {albums.map(album => {
            return <Row style={{ marginBottom: 20 }}>
              <Card style={{ borderRadius: 10 }}>
                <Navbar><strong>{album.artist} - {album.title}</strong></Navbar>
                <CardBody>
                  <CardImg src={album.albumCoverUrl} style={{ width: 150, height: 150 }}></CardImg>
                  <CardSubtitle>Released: {String(album.isReleased)}</CardSubtitle>
                  <CardText>Text</CardText>
                </CardBody>
              </Card>
            </Row>
          })}
        </Container>
      </Container>
    </main>
  );
}

export default Content;