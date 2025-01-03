<script setup lang="ts">

</script>

<template>
  <div class="container mt-5">
    <h3>Log In Here</h3>
    <form name="login">
      <div class="mb-3">
        <label for="exampleInputEmail1" class="form-label">Email address</label>
        <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
      </div>
      <div class="mb-3">
        <label for="exampleInputPassword1" class="form-label">Password</label>
        <input type="password" class="form-control" id="exampleInputPassword1">
      </div>
      <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <div class="mt-5">
      <router-link to="/signup">
        <button type="button" class="btn btn-primary btn-sm">Wanna Sign Up?</button>
      </router-link>
    </div>
  </div>
</template>

<style scoped>

</style>
