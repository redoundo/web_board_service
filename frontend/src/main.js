import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from "./components/js/router.js";
// import {configDotenv} from "dotenv";
// configDotenv({path : "frontend/.env" , encoding : "UTF-8"})

createApp(App)
    .use(router)
    .mount('#app')