generate-sdk:
    docker run --rm -v "$PWD:/local" --user $(id -u):$(id -g) openapitools/openapi-generator-cli generate -c /local/openapi-generator-config.yaml -i /local/ivk-skill-openapi.json -g kotlin -o /local/skill-sdk --enable-post-process-file
    # Fix imports in any new test files (generator uses old kotlintest imports)
    find skill-sdk/src/test -name "*.kt" -exec sed -i 's/io\.kotlintest\.shouldBe/io.kotest.matchers.shouldBe/g' {} \;
    find skill-sdk/src/test -name "*.kt" -exec sed -i 's/io\.kotlintest\.specs\.ShouldSpec/io.kotest.core.spec.style.ShouldSpec/g' {} \;

build-sdk:
    ./gradlew :skill-sdk:build

test:
    ./gradlew :skill-sdk:test
