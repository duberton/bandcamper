import { useEffect, useState } from "react";
import { Label, FormGroup, Input, Button, Container, Row, Form } from "reactstrap";
import { connect } from "react-redux";
import ky from 'ky';

function CreateAlbum(props) {

  const [url, setUrl] = useState('');

  const createAlbum = async (event) => {
    event.preventDefault();
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const response = await ky.post(`${apiUrl}/v1/album`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${props.accessToken}`
      },
      body: JSON.stringify({ "url": url })
    }).json();
    setUrl('');
  }

  const handleInputChange = event => {
    event.preventDefault();
    setUrl(event.target.value);
  }

  return (
    <main>
      <Container fluid style={{ marginTop: 50 }}>
        <Container>
          <Form onSubmit={createAlbum}>
            <FormGroup>
              <Label for="url">Bandcamp album URL</Label>
              <Input type="text" name="url" id="url" placeholder="https://artist.bandcamp.com" value={url}
                onChange={handleInputChange} required pattern="(http|https):\/\/([a-zA-Z0-9]+.bandcamp.com)\/?.?" />
            </FormGroup>
            <Button>Submit</Button>
          </Form>
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

export default connect(mapStateToProps)(CreateAlbum);