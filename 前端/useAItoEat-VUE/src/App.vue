<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import LoginPage from './components/LoginPage.vue'

export default {
  name: 'App',
  components: {
    LoginPage
  }
}
</script>
