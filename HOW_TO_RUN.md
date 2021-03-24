# MacOS build guide

## Steps to run `SsiAgentApiImplTest` test on MacOS

1. Install Homebrew by following the steps described here: https://docs.brew.sh/Installation
2. Install Rust and rustup (https://www.rust-lang.org/install.html).
3. Install required native libraries and utilities
   ```
   brew install pkg-config
   brew install libsodium   
   brew install automake 
   brew install autoconf
   brew install cmake
   brew install openssl
   brew install zeromq
   brew install zmq
   ```
4. Setup environment variables:
   ```
   export PKG_CONFIG_ALLOW_CROSS=1
   export CARGO_INCREMENTAL=1
   export RUST_LOG=indy=trace
   export RUST_TEST_THREADS=1
   ```
5. Setup OPENSSL_DIR variable: path to installed openssl library
   ```
   for version in `ls -t /usr/local/Cellar/openssl/`; do
        export OPENSSL_DIR=/usr/local/Cellar/openssl/$version
        break
   done
   ```
   Note that `/usr/local/Cellar/openssl` folder might have a different name, for example, `/usr/local/Cellar/openssl@1.1/`. 
   In this case, use the correct folder name when setting up OPENSSL_DIR.

6. Checkout and build the library:
   ```
   git clone https://github.com/hyperledger/indy-sdk.git
   cd ./indy-sdk/libindy
   cargo build
   ```

7. Set your `DYLD_LIBRARY_PATH` and `LD_LIBRARY_PATH` environment variables to the path of `indy-sdk/libindy/target/debug`. 
   You may want to put these in your `.bash_profile` to persist them.
   For some MacOS versions, adding those variables to `.bash_profile` doesn't help.  
   As a quick solution, set those variables in Android Studio / Intellij Idea in 'Edit Configurations' for `SsiAgentApiImplTest`.

8. Deploy .NET agent to a Linux machine and run it by executing `docker-compose up  -d` from `playground` folder.

9. Open `WebSocketTransportImpl` class and replace the value of `host` variable by the IP address of the machine where .NET agent is deployed, for example, `val host = "52.14.87.10"`.

10. Run `SsiAgentApiImplTest` test.