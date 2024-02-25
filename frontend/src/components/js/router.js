import {createRouter, createWebHistory} from "vue-router";
import index from '/src/components/vues/index/index.vue'
import view from "/src/components/vues/view/view.vue";
import modify from "/src/components/vues/modify/modify.vue";
import write from "/src/components/vues/write/write.vue";

const routes = [
    {path: "/" , component : index , name : "index"} ,
    {path: "/view" ,component: view , name : "view"} ,
    {path: "/view/modify" , component: modify , name : "modify"} ,
    {path: "/write" , component: write , name : "write"}
];

const router = createRouter({
    history : createWebHistory() ,
    routes : routes
})
export default router;