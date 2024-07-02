import {createApp} from 'vue'
import './style.css'
import App from './App.vue'
import router from "./components/js/router.js";
import VueCookies from "vue-cookies";
// import {configDotenv} from "dotenv";
// configDotenv({path : "frontend/.env" , encoding : "UTF-8"})

createApp(App)
    .use(router)
    .use(VueCookies)
    .mount('#app')