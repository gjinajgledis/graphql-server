package com.example.graphqlserver.config;


import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import graphql.execution.ExecutionStepInfo;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.SimplePerformantInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldCompleteParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class GeneralInstrumentation extends SimplePerformantInstrumentation {
    private final MeterRegistry meterRegistry;

    public GeneralInstrumentation(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public InstrumentationState createState(InstrumentationCreateStateParameters parameters) {
        return new EncounteredFieldsState();
    }

    @Override
    public InstrumentationContext<Object> beginFieldListCompletion(InstrumentationFieldCompleteParameters parameters, InstrumentationState state) {
        String fieldKey = getFieldKey(parameters.getExecutionStepInfo());
        if (!fieldKey.startsWith("__")) {
            //EncounteredFieldsState encounteredFieldsState = (EncounteredFieldsState) state;
            //boolean havePreviousEncounter = encounteredFieldsState.haveEncounteredField(fieldKey);

            //f (!havePreviousEncounter) {
            System.out.println("[List] Got field key:" + fieldKey);

            //encounteredFieldsState.recordFieldEncounter(fieldKey);
            return SimpleInstrumentationContext.whenCompleted((T, U) -> {
                meterRegistry.counter("gql.fieldUsage", "gql.field", fieldKey).increment();
            });

            // }
        }

        return super.beginFieldListCompletion(parameters, state);
    }


    @Override
    public InstrumentationContext<Object> beginFieldExecution(InstrumentationFieldParameters parameters,
                                                              InstrumentationState state) {

        String fieldKey = getFieldKey(parameters.getExecutionStepInfo());
        if (!fieldKey.startsWith("__")) {
            //EncounteredFieldsState encounteredFieldsState = (EncounteredFieldsState) state;
            //boolean havePreviousEncounter = encounteredFieldsState.haveEncounteredField(fieldKey);

            //f (!havePreviousEncounter) {
                System.out.println("Got field key:" + fieldKey);

                //encounteredFieldsState.recordFieldEncounter(fieldKey);
                return SimpleInstrumentationContext.whenCompleted((T, U) -> {
                    meterRegistry.counter("gql.fieldUsage", "gql.field", fieldKey).increment();
                });

           // }
        }

        return super.beginFieldExecution(parameters, state);
    }

    private String getFieldKey(ExecutionStepInfo stepInfo) {
        GraphQLOutputType containingRawType = stepInfo.getParent().getType();
        GraphQLObjectType containingType;

        if (containingRawType instanceof GraphQLNonNull) {
            containingType = (GraphQLObjectType) ((GraphQLNonNull) containingRawType).getWrappedType();
        } else {
            containingType = (GraphQLObjectType) containingRawType;
        }

        return containingType.getName() + "." + stepInfo.getField().getSingleField().getName();
    }

    private static final class EncounteredFieldsState implements InstrumentationState {
        private final Set<String> encounteredFields = new HashSet<>();

        public boolean haveEncounteredField(String fieldKey) {
            return encounteredFields.contains(fieldKey);
        }

        public void recordFieldEncounter(String fieldKey) {
            encounteredFields.add(fieldKey);
        }
    }
}
