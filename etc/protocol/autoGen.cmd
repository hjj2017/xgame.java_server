@echo off

rd /s/q .\out
md .\out

rem # Java
protoc --java_out=..\..\bizserver\src\main\java .\commProtocol.proto

rem # CSharp
protoc --csharp_out=.\out .\commProtocol.proto
