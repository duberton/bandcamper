import './App.css';
import Home from './components/home';
import FollowRelease from './components/follow';
import Header from './components/common/Header';
import { Provider } from 'react-redux';
import { store, persistor } from './redux/store'
import { PersistGate } from 'redux-persist/integration/react';
import { BrowserRouter, Route, Switch } from 'react-router-dom/cjs/react-router-dom.min';
import TimeAgo from 'javascript-time-ago'

import en from 'javascript-time-ago/locale/en'

TimeAgo.addDefaultLocale(en)

function App() {
  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>
        <BrowserRouter>
          <div className="App">
            <Header />
            <Switch>
              <Route path="/" component={Home}></Route>
            </Switch>
          </div>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  );
}

export default App;