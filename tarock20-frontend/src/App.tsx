import './App.css';
import { Route, Routes } from "react-router-dom";
import Lobby from './Lobby/Lobby';
import Game from './Game/Game';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Lobby />} />
        <Route path="/game/:gameId" element={<Game />} />
        <Route
          path="*"
          element={
            <main style={{ padding: "1rem" }}>
              <p>There's nothing here!</p>
            </main>
          }
        />
      </Routes>
    </div>
  )
}

export default App
