import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import axios from "axios";

function selectData(){
    axios.post('http://localhost:8080/testData',["가","나","다"])
        .then(function (res){
            console.log(res)
        });
}

function App() {
  const [count, setCount] = useState(0)

  return (
    <>

        <div className="App">
            <header className="App-header">
                <div>
                    <button onClick={() =>selectData()}>조회</button>
                </div>
            </header>
        </div>
    </>
  )
}

export default App
