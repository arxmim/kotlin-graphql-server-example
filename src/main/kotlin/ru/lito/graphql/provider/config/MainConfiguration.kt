package ru.lito.graphql.provider.config

import com.google.common.io.Resources
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import graphql.spring.web.servlet.GraphQLInvocation
import org.dataloader.DataLoaderRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.lito.graphql.provider.GraphQLDataFetchers


@Configuration
class MainConfiguration {

    @Bean
    fun graphQLDataFetchers(): GraphQLDataFetchers {
        return GraphQLDataFetchers()
    }

    @Bean
    fun dataLoaderRegistry(graphQLDataFetchers: GraphQLDataFetchers): DataLoaderRegistry {
        val registry = DataLoaderRegistry()
        registry.register("batched", graphQLDataFetchers.authorDataLoader)
        return registry
    }

    @Bean
    fun graphQLInvocation(graphQL: GraphQL, dataLoaderRegistry: DataLoaderRegistry): GraphQLInvocation {
        return GraphQLInvocation { invocationData, _ ->
            val executionInput = ExecutionInput.newExecutionInput()
                .query(invocationData.query)
                .dataLoaderRegistry(dataLoaderRegistry)
                .operationName(invocationData.operationName)
                .variables(invocationData.variables)
                .build()
            graphQL.executeAsync(executionInput)
        }
    }

    @Bean
    fun graphQL(graphQLDataFetchers: GraphQLDataFetchers): GraphQL {
        val url = Resources.getResource("schema.graphqls")
        val sdl = Resources.toString(url, Charsets.UTF_8)
        val graphQLSchema = buildSchema(sdl, graphQLDataFetchers)
        return GraphQL.newGraphQL(graphQLSchema).instrumentation(
            DataLoaderDispatcherInstrumentation(
                DataLoaderDispatcherInstrumentationOptions.newOptions().includeStatistics(true)
            )
        ).build()
    }

    private fun buildSchema(sdl: String?, graphQLDataFetchers: GraphQLDataFetchers): GraphQLSchema? {
        val typeDefinitionRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring(graphQLDataFetchers)
        return SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
    }

    private fun buildWiring(graphQLDataFetchers: GraphQLDataFetchers): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
            .type(
                newTypeWiring("Query")
                    .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
            )
            .type(
                newTypeWiring("Author")
                    .dataFetcher("bookCount", graphQLDataFetchers.getAuthorBookCountFetcher())
            )
            .type(
                newTypeWiring("Book")
                    .dataFetcher("author", graphQLDataFetchers.getAuthor())
            )
            .type(
                newTypeWiring("Query")
                    .dataFetcher("mostPopularBooks", graphQLDataFetchers.getMostPopularBooks())
            )
            .build()
    }


}