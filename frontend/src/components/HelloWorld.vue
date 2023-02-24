<template>
    <v-container>
        <v-row class="text-center">
            <v-btn class=”Search__button” @click="callRestService()">CALL Spring Boot REST backend service</v-btn>

            <h3>{{ response }}</h3>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios'

export default {
    name: 'HelloWorld',
    data: () => ({
        msg: 'HowTo call REST-Services:',
        response: '',
        errors: []
    }),
    methods: {
        // Fetches posts when the component is created.
        callRestService () {
            axios.get(`/api/hello`)
                .then(response => {
                    // JSON responses are automatically parsed.
                    this.response = response.data
                })
                .catch(e => {
                    this.errors.push(e)
                })
        }
    }
}
</script>
