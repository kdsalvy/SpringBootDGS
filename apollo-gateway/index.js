const {ApolloServer, gql} = require('apollo-server');
const {ApolloGateway, IntrospectAndCompose} = require('@apollo/gateway')

const gateway = new ApolloGateway({
    supergraphSdl: new IntrospectAndCompose({
        subgraphs: [
            {name: 'students', url: 'http://localhost:4001/graphql'},
            {name: 'books', url: 'http://localhost:4002/graphql'},
        ]
    })
});

const server = new ApolloServer({gateway, subscriptions: false, tracing: true});
// default port is 4000
server.listen();