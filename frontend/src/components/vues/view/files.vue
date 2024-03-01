<script setup>
import axios from "axios";

const props = defineProps({files : Array})
const url = "http://localhost:8080"
async function filesAxios(index){
  if((index??null) !== null && (props.files??null) !== null && props.files.length > 0){
    const data = {filePath : props.files[index]['filePath'] , fileName : props.files[index]['fileName']}
    await axios.post(url + "/view/download" , data)
        .then(value=>{
          console.log(value.data);
        })
        .catch(err=>{console.log(err)})
  }
}

</script>

<template>
  <div>
  <span v-if="(props.files??null) !== null && props.files.length > 0">
      <label v-for="(item , index) in props.files"  v-bind:key="index">다운로드
        <input type="button" v-bind:value="item['fileId']" v-on:click="()=>filesAxios(index)"/>
      </label>
    </span>
  </div>
</template>

<style scoped>

</style>