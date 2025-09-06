Pod::Spec.new do |spec|
    spec.name                     = 'pusher_websocket_kmp'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://github.com/samliddleg/pusher-websocket-kmp'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'KMP integration with PusherSwift'
    spec.vendored_frameworks      = 'build/cocoapods/framework/PusherWebsocketKmp.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '13.0'
    spec.dependency 'PusherSwift', '~> 10.1.6'
                
    if !Dir.exist?('build/cocoapods/framework/PusherWebsocketKmp.framework') || Dir.empty?('build/cocoapods/framework/PusherWebsocketKmp.framework')
        raise "

        Kotlin framework 'PusherWebsocketKmp' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :pusher-websocket-kmp:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':pusher-websocket-kmp',
        'PRODUCT_MODULE_NAME' => 'PusherWebsocketKmp',
    }
                
    spec.script_phases = [
        {
            :name => 'Build pusher_websocket_kmp',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end