require 'test_helper'

class WallsControllerTest < ActionDispatch::IntegrationTest
  setup do
    @wall = walls(:one)
  end

  test "should get index" do
    get walls_url
    assert_response :success
  end

  test "should get new" do
    get new_wall_url
    assert_response :success
  end

  test "should create wall" do
    assert_difference('Wall.count') do
      post walls_url, params: { wall: { name: @wall.name } }
    end

    assert_redirected_to wall_url(Wall.last)
  end

  test "should show wall" do
    get wall_url(@wall)
    assert_response :success
  end

  test "should get edit" do
    get edit_wall_url(@wall)
    assert_response :success
  end

  test "should update wall" do
    patch wall_url(@wall), params: { wall: { name: @wall.name } }
    assert_redirected_to wall_url(@wall)
  end

  test "should destroy wall" do
    assert_difference('Wall.count', -1) do
      delete wall_url(@wall)
    end

    assert_redirected_to walls_url
  end
end
