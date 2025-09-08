generate:
    docker run --rm -v "$PWD:/local" --user $(id -u):$(id -g) openapitools/openapi-generator-cli generate -c /local/openapi-generator-config.yaml -i /local/openapi-spec.json -g kotlin -o /local/skill-sdk --enable-post-process-file
