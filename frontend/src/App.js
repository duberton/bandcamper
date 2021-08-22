import { Provider } from 'react-redux';
import './App.css';
import { store, persistor } from './redux/store'
import Content from './components/home/Content';
import Header from './components/home/Header';
import { PersistGate } from 'redux-persist/integration/react';
import { BrowserRouter, Route, Switch } from 'react-router-dom/cjs/react-router-dom.min';
import CreateAlbum from './components/CreateAlbum';

function App() {
  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>
        <BrowserRouter>
          <div className="App">
            <Header />
          </div>
          <Switch>
            <Route path="/create" component={CreateAlbum}></Route>
            <Route path="/" component={Content}></Route>
          </Switch>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  );
}

export default App;