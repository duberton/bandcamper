import { useEffect, useState } from "react";
import { Label, FormGroup, Input, InputGroupText, InputGroup, InputGroupAddon, Button, Container, Row, Form } from "reactstrap";
import { connect } from "react-redux";
import ky from 'ky';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAngleRight } from '@fortawesome/free-solid-svg-icons'

function FollowRelease(props) {

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
    props.fetchAlbums();
  }

  const handleInputChange = event => {
    event.preventDefault();
    setUrl(event.target.value);
  }

  return (
    <main style={{ width: '100%' }}>
      <Container fluid style={{ marginTop: 50 }}>
        <Container style={{ width: '90%' }}>
          <Form onSubmit={createAlbum}>
            <FormGroup>
              <InputGroup style={{ width: '100%' }}>
                <InputGroupAddon style={{ width: '100%' }} addonType="append">
                  <Input style={{ width: '100%', height: '60px', borderRadius: 0 }} type="text" name="url" id="url" placeholder="https://artist.bandcamp.com OR https://artist.bandcamp.com/release" value={url}
                    onChange={handleInputChange} required pattern="(http|https):\/\/([a-zA-Z0-9]+.bandcamp.com)\/.*" />
                  <InputGroupText onClick={createAlbum}><FontAwesomeIcon icon={faAngleRight}/></InputGroupText>
                  {/* <Label for="url">Bandcamp album URL</Label> */}
                </InputGroupAddon>
              </InputGroup>
            </FormGroup>
            {/* <Button>Submit</Button> */}
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

export default connect(mapStateToProps)(FollowRelease);